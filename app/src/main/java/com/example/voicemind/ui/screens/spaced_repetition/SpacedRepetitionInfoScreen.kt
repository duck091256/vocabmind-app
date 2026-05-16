package com.example.voicemind.ui.screens.spaced_repetition

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SpacedRepetitionInfoScreen(
    onNavigateBack: () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
        ) {
            // 1. Nút Back
            item {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFF5F5F5), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // 2. Section: What is Spaced Repetition?
            item {
                Text(
                    text = "What is Spaced Repetition?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = buildAnnotatedString {
                        append("The basis for spaced repetition research was laid by Hermann Ebbinghaus, who suggested that information loss over time follows a forgetting curve, but that forgetting could be reset with repetition ba... ")
                        withStyle(style = SpanStyle(color = Color.Gray)) {
                            append("See More")
                        }
                    },
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            // 3. Section: Memory levels
            item {
                Text(
                    text = "Memory levels",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = buildAnnotatedString {
                        append("4English divides the memory levels into 6 levels, from level 1 to ")
                        withStyle(style = SpanStyle(color = Color(0xFF673AB7), fontWeight = FontWeight.Bold)) {
                            append("Deep memory")
                        }
                        append(". To improve your memory levels, you need to relearn and remember the word after each interval as shown in the table below. Don't worry, 4English will remind you when it's time to relearn.")
                    },
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // 4. Bảng Memory Levels
            item {
                MemoryLevelsTable()
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 5. Footer Link
            item {
                Text(
                    text = "(*) https://en.wikipedia.org/wiki/Spaced_repetition",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun MemoryLevelsTable() {
    val levels = listOf(
        LevelData(1, "Level 1", "After 2 hour", 0.16f, Color(0xFFE67E22)),
        LevelData(2, "Level 2", "After 1 days", 0.33f, Color(0xFFF1C40F)),
        LevelData(3, "Level 3", "After 2 days", 0.5f, Color(0xFFA9DFBF)),
        LevelData(4, "Level 4", "After 3 days", 0.66f, Color(0xFF27AE60)),
        LevelData(5, "Level 5", "After 5 days", 0.83f, Color(0xFF3498DB)),
        LevelData(6, "Mastered", "After 8 days", 1f, Color(0xFF2980B9))
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF9F9F9))
    ) {
        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEEF2FC))
                .padding(vertical = 16.dp, horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Memory Levels", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("Time to review", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        // Data Rows
        levels.forEachIndexed { index, level ->
            val backgroundColor = if (index % 2 == 0) Color.White else Color(0xFFF5F5F5)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(vertical = 16.dp, horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon & Level Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    LevelProgressIcon(level = level.level.toString(), progress = level.progress, color = level.color)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = level.title, fontSize = 16.sp, color = Color.Black)
                }

                // Time
                Text(
                    text = level.time,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
            }
        }
    }
}

@Composable
fun LevelProgressIcon(
    level: String,
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier.size(28.dp),
    strokeWidth: Float = 4.dp.value,
    fontSize: androidx.compose.ui.unit.TextUnit = 14.sp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val strokePx = strokeWidth * density
            // Vòng tròn nền xám
            drawArc(
                color = Color.LightGray.copy(alpha = 0.4f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokePx)
            )
            // Vòng cung màu thể hiện tiến độ
            if (progress > 0f) {
                drawArc(
                    color = color,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(width = strokePx, cap = StrokeCap.Round)
                )
            }
        }
        Text(
            text = level,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

data class LevelData(
    val level: Int,
    val title: String,
    val time: String,
    val progress: Float,
    val color: Color
)

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Spaced Repetition Screen Preview"
)
@Composable
fun SpacedRepetitionScreenPreview() {
    MaterialTheme {
        SpacedRepetitionInfoScreen(onNavigateBack = {})
    }
}