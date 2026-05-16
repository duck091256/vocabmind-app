package com.example.voicemind.ui.screens.sets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.voicemind.R
import com.example.voicemind.domain.model.VocabSet
import com.example.voicemind.domain.model.Word
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.Date
import androidx.compose.ui.platform.LocalLocale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySetsScreen(
    onNavigateToDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MySetsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var setToDelete by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is SetsUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is SetsUiEvent.NavigateToDetail -> {
                    onNavigateToDetail(event.setId)
                }
            }
        }
    }
    setToDelete?.let { setId ->
        DeleteConfirmationDialog(
            onDismiss = { setToDelete = null },
            onConfirm = {
                viewModel.onAction(SetsUiAction.DeleteSet(setId))
                setToDelete = null
            }
        )
    }

    var setToRename by remember { mutableStateOf<VocabSet?>(null) }
    setToRename?.let { vocabSet ->
        RenameSetDialog(
            vocabSet = vocabSet,
            onDismiss = { setToRename = null },
            onConfirm = { newTitle, newDescription ->
                // TODO: Implement RenameSet in ViewModel
                snackbarHostState.currentSnackbarData?.dismiss()
                // viewModel.onAction(SetsUiAction.RenameSet(vocabSet.id, newTitle, newDescription))
                setToRename = null
            }
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
        ) {
            StudySetsHeader(
                totalCount = uiState.sets.size,
                modifier = Modifier.padding(20.dp)
            )

            SearchBarAndSort(
                query = uiState.searchQuery,
                onQueryChange = { viewModel.onAction(SetsUiAction.SearchQueryChanged(it)) },
                sortOption = uiState.sortOption,
                onSortChange = { viewModel.onAction(SetsUiAction.SortOptionChanged(it)) },
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
                }
            } else if (uiState.sets.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)  // -> Chừa ra 1 khoảng để bottomNav k che vào
                ) {
                    items(uiState.sets, key = { it.id }) { set ->
                        SetCard(
                            vocabSet = set,
                            onClick = { onNavigateToDetail(set.id) },
                            onRename = { setToRename = set },
                            onDelete = { setToDelete = set.id }
                        )
                    }
                }
            }
        }
        
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun StudySetsHeader(totalCount: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.study_sets),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(12.dp))
            Surface(
                color = Color(0xFFEDE7F6),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.total_count, totalCount),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6200EE)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.study_sets_desc),
            fontSize = 14.sp,
            color = Color.Gray,
            lineHeight = 20.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarAndSort(
    query: String,
    onQueryChange: (String) -> Unit,
    sortOption: SortOption,
    onSortChange: (SortOption) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text(stringResource(R.string.search_sets_placeholder)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            modifier = Modifier.weight(1f)
        )
    }
    
    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {

        Box(
            contentAlignment = Alignment.CenterEnd
        ) {

            Row(
                modifier = Modifier
                    .clickable { expanded = true },
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Sort By: ",
                    color = Color(0xFF6200EE)
                )

                Text(
                    text = sortOption.name.replace("_", " "),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6200EE)
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Sort Options",
                    tint = Color(0xFF6200EE)

                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                SortOption.values().forEach { option ->

                    DropdownMenuItem(
                        text = {
                            Text(
                                option.name.replace("_", " ")
                            )
                        },
                        onClick = {
                            onSortChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.book),
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = Color.LightGray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No study sets found",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tap the + button to create a new set",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SetCard(
    vocabSet: VocabSet,
    onClick: () -> Unit,
    onRename: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = SimpleDateFormat("dd MMM yyyy", LocalLocale.current.platformLocale)
    val dateString = formatter.format(Date(vocabSet.createdAt))

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 0.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE6D9FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        /*painter = painterResource(
                            id = vocabSet.imageRes ?: R.drawable.ic_folder
                        ), -> Sẽ dùng sau khi cập nhật lại thêm field imageRes trong VocabSet*/
                        painter = painterResource(R.drawable.ic_folder), // -> Tạm thời
                        contentDescription = "Vocab Set Image",
                        modifier = Modifier.size(32.dp),
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = vocabSet.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C1E)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = vocabSet.description,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 2
                    )
                }
                var expanded by remember { mutableStateOf(false) }
                Box(
                    contentAlignment = Alignment.CenterEnd
                ) {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            modifier = Modifier.size(32.dp),
                            tint = Color.Gray
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Rename") },
                            onClick = {
                                expanded = false
                                onRename()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                            onClick = {
                                expanded = false
                                onDelete()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Cancel") },
                            onClick = {
                                expanded = false
                            }
                        )
                    }
                }
            }
//            Spacer(modifier = Modifier.height(16.dp))
/*            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${vocabSet.totalWords} words",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = dateString,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }*/
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSetDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Set") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        titleError = false
                    },
                    label = { Text("Title") },
                    isError = titleError,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (titleError) {
                    Text("Title cannot be empty", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isBlank()) {
                        titleError = true
                    } else {
                        onConfirm(title, description)
                    }
                }
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun DeleteConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Set") },
        text = { Text("Are you sure you want to delete this study set? This action cannot be undone.") },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenameSetDialog(
    vocabSet: VocabSet,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(vocabSet.title) }
    var description by remember { mutableStateOf(vocabSet.description) }
    var titleError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Rename Set") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        titleError = false
                    },
                    label = { Text("Title") },
                    isError = titleError,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (titleError) {
                    Text("Title cannot be empty", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isBlank()) {
                        titleError = true
                    } else {
                        onConfirm(title, description)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MySetsScreenPreview() {
    val JLPTWords = listOf(
        Word(
            id = "w1",
            setId = "1",
            word = "こんにちは",
            meaning = "Xin chào",
            example = "こんにちは、元気ですか？"
        ),
        Word(
            id = "w2",
            setId = "1",
            word = "ありがとう",
            meaning = "Cảm ơn",
            example = "助けてくれてありがとう。"
        ),
        Word(
            id = "w3",
            setId = "1",
            word = "学生",
            meaning = "Học sinh / Sinh viên",
            example = "私は学生です。"
        )
    )

    val ieltsWords = listOf(
        Word(
            id = "w4",
            setId = "2",
            word = "Analyze",
            meaning = "Phân tích",
            example = "Students should analyze the chart carefully."
        ),
        Word(
            id = "w5",
            setId = "2",
            word = "Significant",
            meaning = "Đáng kể",
            example = "There was a significant increase in population."
        ),
        Word(
            id = "w6",
            setId = "2",
            word = "Approach",
            meaning = "Phương pháp",
            example = "This approach is widely used in education."
        )
    )

    val businessWords = listOf(
        Word(
            id = "w7",
            setId = "3",
            word = "Deadline",
            meaning = "Hạn chót",
            example = "We must finish the project before the deadline."
        ),
        Word(
            id = "w8",
            setId = "3",
            word = "Negotiation",
            meaning = "Đàm phán",
            example = "The negotiation lasted for two hours."
        ),
        Word(
            id = "w9",
            setId = "3",
            word = "Revenue",
            meaning = "Doanh thu",
            example = "Company revenue increased this quarter."
        )
    )

    // Fake data preview
    val previewSets = listOf(
        VocabSet(
            id = "1",
            title = "JLPT N5 Vocabulary",
            description = "Basic Japanese vocabulary",
            totalWords = JLPTWords.size,
            createdAt = System.currentTimeMillis(),
            userId = "preview_user",
            words = JLPTWords
        ),
        VocabSet(
            id = "2",
            title = "IELTS Academic",
            description = "Common IELTS vocabulary",
            totalWords = ieltsWords.size,
            createdAt = System.currentTimeMillis(),
            userId = "preview_user",
            words = ieltsWords
        ),

        VocabSet(
            id = "3",
            title = "Business English",
            description = "Words used in meetings and emails",
            totalWords = businessWords.size,
            createdAt = System.currentTimeMillis(),
            userId = "preview_user",
            words = businessWords
        )
    )

    val previewState = SetsUiState(
        sets = previewSets,
        searchQuery = "",
        sortOption = SortOption.NEWEST,
        isLoading = false,
        error = null
    )

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
        ) {

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                StudySetsHeader(
                    totalCount = previewState.sets.size,
                    modifier = Modifier.padding(20.dp)
                )

                SearchBarAndSort(
                    query = previewState.searchQuery,
                    onQueryChange = {},
                    sortOption = previewState.sortOption,
                    onSortChange = {},
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(previewState.sets, key = { it.id }) { set ->
                        SetCard(
                            vocabSet = set,
                            onClick = {},
                            onRename = {},
                            onDelete = {}
                        )
                    }
                }
            }
        }
    }
}