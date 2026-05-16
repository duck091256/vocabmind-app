package com.example.voicemind.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.voicemind.R
import com.example.voicemind.domain.model.ForgettingStats
import com.example.voicemind.ui.navigation.NavRoute
import com.example.voicemind.ui.theme.VocabMindTheme

@Composable
fun HomeDashboard(
    navController: NavController,
    viewModel: HomeDashboardViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToReview: () -> Unit,
    modifier: Modifier = Modifier
) {
    val displayName by viewModel.displayName.collectAsStateWithLifecycle()
    val forgettingStats by viewModel.forgettingStats.collectAsStateWithLifecycle()
    val reviewCount by viewModel.reviewCount.collectAsStateWithLifecycle()
    val streak by viewModel.streak.collectAsStateWithLifecycle()

    HomeDashboardContent(
        userName = displayName ?: "User",
        onNavigateToLogin = onNavigateToLogin,
        onNavigateToGame = { navController.navigate(NavRoute.WORD_CHAIN_GAME) },
        onNavigateToLessons = { navController.navigate("lessons") },
        onNavigateToReview = onNavigateToReview,
        forgettingStats = forgettingStats,
        reviewCount = reviewCount,
        streakDays = streak,
        modifier = modifier
    )
}

@Composable
fun HomeDashboardContent(
    userName: String,
    onNavigateToLogin: () -> Unit,
    onNavigateToGame: () -> Unit,
    onNavigateToLessons: () -> Unit,
    onNavigateToReview: () -> Unit,
    forgettingStats: ForgettingStats,
    reviewCount: Int = 0,
    modifier: Modifier = Modifier,
    streakDays: Int = 0,
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

        ForgettingCurveSection(stats = forgettingStats)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onNavigateToReview,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text("📚 Ôn tập từ vựng ($reviewCount từ cần ôn)")
        }
        Spacer(modifier = Modifier.height(32.dp))
        QuickActionsSection(
            onNavigateToGame = onNavigateToGame,
            onNavigateToLessons = onNavigateToLessons
        )
        Spacer(modifier = Modifier.height(32.dp))
        StreakSection(days = streakDays)
        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ==================== PHẦN BIỂU ĐỒ CỘT (ĐƯỜNG CONG LÃNG QUÊN) ====================

// removed local ForgettingStats data class

@Composable
fun ForgettingCurveSection(stats: ForgettingStats) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "📊 Đường cong lãng quên",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1C1E)
            )
            Text(
                text = "Chi tiết",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF6200EE),
                modifier = Modifier.clickable { }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Số từ cần ôn lại theo thời gian",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Biểu đồ cột ngang (dạng column chart)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val maxVal = maxOf(stats.level1, stats.level2, stats.level3, stats.level4, stats.level5, stats.mastered, 1)
            BarColumn(
                label = "2 giờ",
                value = stats.level1,
                maxValue = maxVal,
                color = Color(0xFFEF4444)
            )
            BarColumn(
                label = "1 ngày",
                value = stats.level2,
                maxValue = maxVal,
                color = Color(0xFFF59E0B)
            )
            BarColumn(
                label = "2 ngày",
                value = stats.level3,
                maxValue = maxVal,
                color = Color(0xFF10B981)
            )
            BarColumn(
                label = "3 ngày",
                value = stats.level4,
                maxValue = maxVal,
                color = Color(0xFF3B82F6)
            )
            BarColumn(
                label = "5 ngày",
                value = stats.level5,
                maxValue = maxVal,
                color = Color(0xFF8B5CF6)
            )
            BarColumn(
                label = "Master",
                value = stats.mastered,
                maxValue = maxVal,
                color = Color(0xFF673AB7)
            )
        }
    }
}

@Composable
fun BarColumn(
    label: String,
    value: Int,
    maxValue: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    val heightFraction = if (maxValue > 0) value.toFloat() / maxValue else 0f
    val barHeight = (heightFraction * 150).dp // chiều cao tối đa 150dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(45.dp)
    ) {
        // Giá trị số
        Text(
            text = value.toString(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(4.dp))
        // Cột
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Nhãn
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
            maxLines = 2,
            softWrap = true
        )
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
            Box(modifier = Modifier.size(27.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.notification),
                    contentDescription = "Notifications",
                    modifier = Modifier.size(24.dp).align(Alignment.Center),
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
private fun QuickActionsSection(
    onNavigateToGame: () -> Unit,
    onNavigateToLessons: () -> Unit
) {
    Column {
        Text(
            text = "Quick Actions",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1C1E)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Card Học từ mới
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToLessons() },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF6200EE))
        ) {
            Row(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "📚 Học từ mới",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "100 từ cơ bản, 10 bài học",
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
                        painter = painterResource(id = R.drawable.book),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Card Nối từ (Game)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToGame() },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF03DAC5))
        ) {
            Row(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "🎮 Game nối từ",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Thách thức vốn từ của bạn",
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
                        painter = painterResource(id = R.drawable.quiz_icon),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun StreakSection(days: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE0B2))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFFFB74D), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.fire_svgrepo_com),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "$days Days Streak!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE65100)
                )
                Text(
                    text = "You are on fire! Keep it up.",
                    fontSize = 14.sp,
                    color = Color(0xFFEF6C00)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeDashboardPreview() {
    VocabMindTheme {
        HomeDashboardContent(
            userName = "Alex",
            onNavigateToLogin = {},
            onNavigateToGame = {},
            onNavigateToLessons = {},
            onNavigateToReview = {},
            forgettingStats = ForgettingStats(10, 5, 2, 8, 20, 15),
            reviewCount = 15
        )
    }
}
