package com.example.voicemind.ui.screens.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.voicemind.R
import com.example.voicemind.ui.navigation.AppBottomNavigationBar
import com.example.voicemind.ui.navigation.NavRoute
import com.example.voicemind.ui.theme.VocabMindTheme

@Composable
fun HomeDashboard(
    viewModel: HomeDashboardViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val displayName by viewModel.displayName.collectAsStateWithLifecycle()

    HomeDashboardContent(
        userName = displayName ?: "User",
        onNavigateToLogin = onNavigateToLogin,
        modifier = modifier
    )
}

@Composable
fun HomeDashboardContent(
    userName: String,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    streakDays: Int = 12,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        HeaderSection(userName, onNavigateToLogin)
        Spacer(modifier = Modifier.height(32.dp))
        RecentVocabSetsSection()
        Spacer(modifier = Modifier.height(32.dp))
        QuickActionsSection()
        Spacer(modifier = Modifier.height(32.dp))
        StreakSection(streakDays)
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun HeaderSection(userName: String, onLogout: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFD591)),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder for profile image
                Image(
                    painter = painterResource(id = R.drawable.man),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp).clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = stringResource(R.string.welcome_back),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = stringResource(R.string.hello_user, userName),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                )
            }
        }
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.size(27.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.notification),
                    contentDescription = "Notifications",
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center),
                    tint = Color(0xFF1A1C1E)
                )

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color(0xFF6200EE), CircleShape)
                        .align(Alignment.TopEnd)
                )
            }
        }
    }
}

@Composable
private fun RecentVocabSetsSection() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.recent_vocab_sets),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1C1E)
            )
            Text(
                text = stringResource(R.string.see_all),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF6200EE),
                modifier = Modifier.clickable { }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(end = 20.dp)
        ) {
            items(sampleVocabSets) { set ->
                VocabSetCard(set)
            }
        }
    }
}

@Composable
private fun VocabSetCard(vocabSet: VocabSet) {
    Card(
        modifier = Modifier.width(200.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(vocabSet.color)
            ) {
                // Placeholder for set image
                Image(
                    painter = ColorPainter(vocabSet.color.copy(alpha = 0.5f)),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = vocabSet.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1C1E)
            )
            Text(
                text = stringResource(R.string.words_count, vocabSet.wordCount),
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { if (vocabSet.progress >= 0.99f) 1f else vocabSet.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(50)),
                color = Color(0xFF6200EE),
                trackColor = Color(0xFFF1F3F5),
            )
        }
    }
}

@Composable
private fun QuickActionsSection() {
    Column {
        Text(
            text = "Quick Actions",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1C1E)
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        // AI Generate Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF6200EE))
        ) {
            Row(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.ai_generate),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = stringResource(R.string.ai_generate_desc),
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.sparkle),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Color.White
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(modifier = Modifier.fillMaxWidth()) {
            QuickActionSmallCard(
                iconResId = R.drawable.book,
                title = stringResource(R.string.my_sets),
                description = stringResource(R.string.my_sets_desc),
                iconColor = Color(0xFFEBE3FF),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            QuickActionSmallCard(
                iconResId = R.drawable.energy,
                title = stringResource(R.string.quick_quiz),
                description = stringResource(R.string.quick_quiz_desc),
                iconColor = Color(0xFFF3E8FF),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickActionSmallCard(
    @DrawableRes iconResId: Int,
    title: String,
    description: String,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF6200EE)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1C1E)
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun StreakSection(days: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.todays_streak),
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.6f)
                )
                Text(
                    text = stringResource(R.string.days_active, days),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                StreakIcon(Color(0xFF8B5CF6))
                Spacer(modifier = Modifier.width(8.dp))
                StreakIcon(Color(0xFF4F46E5))
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun StreakIcon(color: Color) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.fire_svgrepo_com),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color.White
        )
    }
}

@Composable
private fun BottomNavigationBar() {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = R.drawable.home), contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text(stringResource(R.string.home)) },
            selected = true,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF6200EE),
                unselectedIconColor = Color.Gray,
                selectedTextColor = Color(0xFF6200EE),
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = R.drawable.dictionary), contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text(stringResource(R.string.sets)) },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = R.drawable.user), contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text(stringResource(R.string.profile)) },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = R.drawable.setting), contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text(stringResource(R.string.settings)) },
            selected = false,
            onClick = { }
        )
    }
}

data class VocabSet(
    val title: String,
    val wordCount: Int,
    val progress: Float,
    val color: Color
)

private val sampleVocabSets = listOf(
    VocabSet("English Essentials", 45, 0.6f, Color(0xFFC5D1B7)),
    VocabSet("IELTS Academic", 120, 0.3f, Color(0xFFFFB74D))
)

@Preview(showBackground = true)
@Composable
fun HomeDashboardPreview() {
    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(
                currentRoute = NavRoute.HOME,
                onNavigate = { }
            )
        }
    ) { _ ->
        VocabMindTheme {
            HomeDashboardContent(
                userName = "Duck",
                onNavigateToLogin = {}
            )
        }
    }
}