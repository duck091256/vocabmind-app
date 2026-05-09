package com.example.voicemind.ui.screens.sets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.voicemind.R
import com.example.voicemind.ui.theme.VocabMindTheme

data class WordItem(
    val term: String,
    val definition: String,
    val status: String,
    val isMastered: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetDetailScreen(
    onBackClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val words = listOf(
        WordItem("Cognitive", "(thuộc) nhận thức", "Mastered", true),
        WordItem("Perception", "sự nhận thức, tri giác", "Learning", false),
        WordItem("Dissonance", "sự bất hòa, sự thiếu hòa hợp", "Learning", false)
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(R.string.advanced_psychology),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF673AB7)
                            )
                        )
                    }
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
                    IconButton(onClick = { }) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(4.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add_ellipse),
                                contentDescription = null,
                                tint = Color(0xFF673AB7),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                },
                windowInsets = WindowInsets(0),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF8F9FE)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            ProgressCard(progress = 0.75f, totalWords = 120)
            Spacer(modifier = Modifier.height(24.dp))
            TabSection(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(words) { word ->
                    WordItemCard(word = word)
                }
            }
        }
    }
}

@Composable
fun ProgressCard(
    progress: Float,
    totalWords: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.set_progress),
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1C1E)
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.mastered),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.total_words),
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = totalWords.toString(),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF673AB7)
                            )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF673AB7),
                trackColor = Color(0xFFF0F0F0),
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun TabSection(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        stringResource(R.string.words),
        stringResource(R.string.flashcards),
        stringResource(R.string.practice)
    )
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFFF0F2F5),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTab == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) Color.White else Color.Transparent)
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color(0xFF673AB7) else Color.Gray
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun WordItemCard(
    word: WordItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.height(80.dp)
        ) {
            if (!word.isMastered) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxSize()
                        .background(color = Color(0xFF673AB7))
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = word.term,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1C1E)
                        )
                    )
                    Text(
                        text = word.definition,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray
                        )
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusBadge(status = word.status, isMastered = word.isMastered)
                    Spacer(modifier = Modifier.width(16.dp))
                    IconButton(onClick = { }, modifier = Modifier.size(24.dp)) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color.Gray.copy(alpha = 0.05f), RoundedCornerShape(4.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(onClick = {}){
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_sound),
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(32.dp)
                                    // TODO: Thêm hiệu ứng nhấn thì chuyển màu
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(
    status: String,
    isMastered: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (isMastered) Color(0xFFEDE7F6) else Color(0xFFF1F4F8),
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = if (isMastered) Color(0xFF673AB7) else Color.Gray
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SetDetailScreenPreview() {
    VocabMindTheme {
        SetDetailScreen()
    }
}