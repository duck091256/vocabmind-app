package com.example.voicemind.ui.screens.sets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.voicemind.R
import com.example.voicemind.ui.navigation.AppBottomNavigationBar
import com.example.voicemind.ui.navigation.NavRoute
import com.example.voicemind.ui.theme.VocabMindTheme

// TopBar expose ra ngoài để NavGraph gắn vào Scaffold chung
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudySetsTopBar() {
    Surface(
        shadowElevation = 2.dp,
        color = Color.White
    ) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFD591)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.man),
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            },
            actions = {
                Icon(
                    painter = painterResource(id = R.drawable.sparkle),
                    contentDescription = null,
                    tint = Color(0xFF6200EE),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(24.dp)
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )
    }
}

// Content thuần — không Scaffold, không topBar/bottomBar bên trong
@Composable
fun StudySetsScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            StudySetsHeader()
            Spacer(modifier = Modifier.height(24.dp))
            StudySetsSearchBar()
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(sampleStudySets) { studySet ->
            StudySetCard(studySet)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            AiUpsellCard()
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun StudySetsHeader() {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.study_sets),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1C1E)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Surface(
                color = Color(0xFFEDE7F6),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.total_count, 12),
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

@Composable
private fun StudySetsSearchBar() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.search_sets_placeholder),
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun StudySetCard(studySet: StudySet) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(studySet.iconBackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = studySet.iconResId),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = studySet.accentColor
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = studySet.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1C1E)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${stringResource(R.string.terms_count, studySet.termCount)} • ${stringResource(R.string.created_time, studySet.createdTime)}",
                fontSize = 13.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.percent_mastered, (studySet.progress * 100).toInt()),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = studySet.accentColor
                )
                Text(
                    text = stringResource(R.string.words_mastered_ratio, studySet.masteredCount, studySet.termCount),
                    fontSize = 11.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { studySet.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = studySet.accentColor,
                trackColor = studySet.accentColor.copy(alpha = 0.1f),
            )
        }
    }
}

@Composable
private fun AiUpsellCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF6200EE))
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Icon(
                painter = painterResource(id = R.drawable.energy),
                contentDescription = null,
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 20.dp, y = 40.dp),
                tint = Color.White.copy(alpha = 0.1f)
            )
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = stringResource(R.string.need_more_focus),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.ai_upsell_desc),
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.try_ai_generate),
                        color = Color(0xFF6200EE),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private data class StudySet(
    val title: String,
    val termCount: Int,
    val createdTime: String,
    val progress: Float,
    val masteredCount: Int,
    val iconResId: Int,
    val iconBackgroundColor: Color,
    val accentColor: Color
)

private val sampleStudySets = listOf(
    StudySet(
        title = "Advanced Psychology",
        termCount = 48,
        createdTime = "2 days ago",
        progress = 0.75f,
        masteredCount = 36,
        iconResId = R.drawable.book,
        iconBackgroundColor = Color(0xFFEDE7F6),
        accentColor = Color(0xFF6200EE)
    ),
    StudySet(
        title = "Molecular Biology",
        termCount = 112,
        createdTime = "1 week ago",
        progress = 0.30f,
        masteredCount = 34,
        iconResId = R.drawable.ic_microscope,
        iconBackgroundColor = Color(0xFFF1F5F9),
        accentColor = Color(0xFF0F172A)
    ),
    StudySet(
        title = "Business French",
        termCount = 24,
        createdTime = "3 weeks ago",
        progress = 0.92f,
        masteredCount = 22,
        iconResId = R.drawable.language,
        iconBackgroundColor = Color(0xFFFEF2F2),
        accentColor = Color(0xFF991B1B)
    ),
    StudySet(
        title = "GRE Verbal Prep",
        termCount = 250,
        createdTime = "1 month ago",
        progress = 0.15f,
        masteredCount = 38,
        iconResId = R.drawable.sparkle,
        iconBackgroundColor = Color(0xFFEDE7F6),
        accentColor = Color(0xFF6200EE)
    )
)

@Preview(showBackground = true)
@Composable
fun StudySetsScreenPreview() {
    VocabMindTheme {
        Scaffold(
            topBar = { StudySetsTopBar() },
            bottomBar = {
                AppBottomNavigationBar(
                    currentRoute = NavRoute.SETS,
                    onNavigate = { }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { },
                    containerColor = Color(0xFF6200EE),
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.add_set),
                        contentDescription = "Add Set",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        ) { paddingValues ->
            StudySetsScreen(modifier = Modifier.padding(paddingValues))
        }
    }
}