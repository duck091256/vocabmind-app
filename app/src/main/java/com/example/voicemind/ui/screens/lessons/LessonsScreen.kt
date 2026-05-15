package com.example.voicemind.ui.screens.lessons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.voicemind.R
import com.example.voicemind.domain.model.Lesson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonsScreen(
    onLessonClick: (Lesson) -> Unit,
    onBackToDashboard: () -> Unit,
    viewModel: LessonsViewModel = hiltViewModel()
) {
    val lessons by viewModel.lessons.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    // Tự động refresh trạng thái mỗi khi màn hình hiển thị lại
    LaunchedEffect(Unit) {
        viewModel.refreshCompletedStatus()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Bài học của tôi",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C1E)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackToDashboard) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color(0xFF6200EE)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA))
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF6200EE)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(lessons) { lesson ->
                        val isCompleted = viewModel.isLessonCompleted(lesson.id)
                        val isUnlocked = viewModel.isLessonUnlocked(lesson.id)
                        LessonCard(
                            lesson = lesson,
                            isCompleted = isCompleted,
                            isUnlocked = isUnlocked,
                            onClick = {
                                if (isUnlocked) {
                                    onLessonClick(lesson)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LessonCard(
    lesson: Lesson,
    isCompleted: Boolean,
    isUnlocked: Boolean,
    onClick: () -> Unit
) {
    val cardColor = when {
        isCompleted -> Color(0xFFE8F5E9)      // xanh nhạt
        !isUnlocked -> Color(0xFFF5F5F5)     // xám nhạt
        else -> Color.White
    }
    val borderColor = when {
        isCompleted -> Color(0xFF4CAF50)
        !isUnlocked -> Color(0xFFBDBDBD)
        else -> Color(0xFFE0E0E0)
    }
    val contentColor = when {
        isCompleted -> Color(0xFF2E7D32)
        !isUnlocked -> Color(0xFF9E9E9E)
        else -> Color(0xFF1A1C1E)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isUnlocked) { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon bài học
            val iconRes = when {
                lesson.title.contains("personality") -> R.drawable.man
                lesson.title.contains("emotion") -> R.drawable.energy
                lesson.title.contains("adverb") -> R.drawable.sparkle
                lesson.title.contains("verb") -> R.drawable.book
                lesson.title.contains("education") -> R.drawable.dictionary
                lesson.title.contains("animals") -> R.drawable.fire_svgrepo_com
                lesson.title.contains("color") -> R.drawable.book
                else -> R.drawable.dictionary
            }
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF6200EE).copy(alpha = if (isUnlocked) 0.1f else 0.05f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = if (isUnlocked) Color(0xFF6200EE) else Color(0xFFBDBDBD)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lesson.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Text(
                    text = "${lesson.words.size} từ vựng",
                    fontSize = 14.sp,
                    color = if (isUnlocked) Color.Gray else Color(0xFFBDBDBD)
                )
            }
            // Badge trạng thái
            when {
                isCompleted -> {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Hoàn thành",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(28.dp)
                    )
                }
                !isUnlocked -> {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Chưa mở khóa",
                        tint = Color(0xFFBDBDBD),
                        modifier = Modifier.size(24.dp)
                    )
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📘", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}