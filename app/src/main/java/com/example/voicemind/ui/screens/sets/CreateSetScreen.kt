package com.example.voicemind.ui.screens.sets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.voicemind.R
import com.example.voicemind.ui.theme.VocabMindTheme
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.util.UUID

data class TermInput(
    val id: String = UUID.randomUUID().toString(),
    var term: String = "",
    var definition: String = ""
)

@Composable
fun CreateSetScreen(
    onNavigateBack: () -> Unit = {},
    onSaveSuccess: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: CreateSetScreenViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is CreateSetUiEvent.Success -> onSaveSuccess()
                is CreateSetUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    val existingSet by viewModel.existingSet.collectAsStateWithLifecycle()

    CreateSetContent(
        onNavigateBack = onNavigateBack,
        onSaveClick = { name, desc, wordList ->
            viewModel.createOrAddWords(name, desc, wordList)
        },
        snackbarHostState = snackbarHostState,
        existingSet = existingSet,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSetContent(
    onNavigateBack: () -> Unit,
    onSaveClick: (String, String, List<Pair<String, String>>) -> Unit,
    snackbarHostState: SnackbarHostState,
    existingSet: com.example.voicemind.domain.model.VocabSet? = null,
    modifier: Modifier = Modifier
) {
    var setName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isPublic by remember { mutableStateOf(false) }

    LaunchedEffect(existingSet) {
        if (existingSet != null) {
            setName = existingSet.title
            description = existingSet.description
        }
    }

    val terms = remember {
        mutableStateListOf(TermInput(), TermInput())
    }

    Box(modifier = modifier.fillMaxSize().background(Color(0xFFF8F9FE))) {
        Column(modifier = Modifier.fillMaxSize()) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (existingSet != null) "Add New Vocabulary" else stringResource(R.string.create_new_set),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF673AB7)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF4B5563)
                        )
                    }
                },
                actions = {
                    TextButton(onClick = {
                        val wordList = terms.filter { it.term.isNotBlank() || it.definition.isNotBlank() }
                            .map { Pair(it.term, it.definition) }
                        onSaveClick(setName, description, wordList)
                    }) {
                        Text(
                            text = if (existingSet != null) "Add" else stringResource(R.string.save),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF673AB7)
                        )
                    }
                },
                windowInsets = WindowInsets(0),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    SetInfoSection(
                        setName = setName,
                        onSetNameChange = { setName = it },
                        description = description,
                        onDescriptionChange = { description = it },
                        isPublic = isPublic,
                        onPublicChange = { isPublic = it },
                        enabled = existingSet == null
                    )
                }

                item {
                    AiGeneratorSection()
                }

                item {
                    TermsHeaderSection(termCount = terms.size)
                }

                items(terms.size, key = { index -> terms[index].id }) { index ->
                    TermDefinitionCard(
                        term = terms[index].term,
                        onTermChange = { terms[index] = terms[index].copy(term = it) },
                        definition = terms[index].definition,
                        onDefinitionChange = { terms[index] = terms[index].copy(definition = it) },
                        onDelete = { terms.removeAt(index) },
                        index = index + 1
                    )
                }

                item {
                    AddRowButton(onClick = { terms.add(TermInput()) })
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
fun SetInfoSection(
    setName: String,
    onSetNameChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    isPublic: Boolean,
    onPublicChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.set_name),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF757575)
                )
                OutlinedTextField(
                    value = setName,
                    onValueChange = onSetNameChange,
                    enabled = enabled,
                    placeholder = { Text(stringResource(R.string.set_name_placeholder), color = Color.LightGray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF3F4F9),
                        focusedContainerColor = Color(0xFFF3F4F9),
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.description_optional),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF757575)
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    enabled = enabled,
                    placeholder = { Text(stringResource(R.string.description_placeholder), color = Color.LightGray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF3F4F9),
                        focusedContainerColor = Color(0xFFF3F4F9),
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.public_set),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.public_set_desc),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Switch(
                    checked = isPublic,
                    onCheckedChange = onPublicChange,
                    enabled = enabled,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF673AB7)
                    )
                )
            }
        }
    }
}

@Composable
fun AiGeneratorSection(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF673AB7),
                        Color(0xFF6200EE)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Surface(
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(id = R.drawable.sparkle),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.ai_generator),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = stringResource(R.string.ai_generator_desc),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.upload),
                    contentDescription = null,
                    tint = Color(0xFF673AB7),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.upload),
                    color = Color(0xFF673AB7),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TermsHeaderSection(modifier: Modifier = Modifier, termCount: Int = 0) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.terms_and_definitions),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.terms_count, termCount),
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermDefinitionCard(
    modifier: Modifier = Modifier,
    term: String = "",
    onTermChange: (String) -> Unit = {},
    definition: String = "",
    onDefinitionChange: (String) -> Unit = {},
    onDelete: () -> Unit = {},
    index: Int = 1
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header của Card (Số thứ tự hoặc nút xóa)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Word $index",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color(0xFF673AB7),
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_trash_bin),
                        contentDescription = "Delete",
                        tint = Color.Red.copy(alpha = 0.6f)
                    )
                }
            }

            // Input cho Term
            ModernInputField(
                label = stringResource(R.string.term),
                placeholder = stringResource(R.string.enter_term),
                value = term,
                onValueChange = onTermChange
            )

            // Input cho Definition
            ModernInputField(
                label = stringResource(R.string.definition),
                placeholder = stringResource(R.string.enter_definition),
                isSingleLine = false,
                value = definition,
                onValueChange = onDefinitionChange
            )
        }
    }
}

@Composable
fun ModernInputField(
    label: String,
    placeholder: String,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    isSingleLine: Boolean = true
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray,
            modifier = Modifier.padding(start = 4.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.Gray.copy(alpha = 0.6f)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = isSingleLine,
            minLines = if (isSingleLine) 1 else 2,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF3F4F9),
                focusedContainerColor = Color(0xFFF3F4F9),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
fun AddRowButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3F4F9)),
            shape = RoundedCornerShape(24.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = null,
                tint = Color(0xFF673AB7),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.add_row),
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateSetScreenPreview() {
    VocabMindTheme {
        Scaffold { paddingValues ->
            CreateSetContent(
                onNavigateBack = {},
                onSaveClick = { _, _, _ -> },
                snackbarHostState = remember { SnackbarHostState() },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}