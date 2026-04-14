package com.example.voicemind.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.voicemind.domain.model.EnglishLevel
import com.example.voicemind.domain.model.LearningGoal
import com.example.voicemind.ui.viewmodel.OnboardingViewModel

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var currentStep by remember { mutableIntStateOf(0) }
    val totalSteps = 5
    val topics = listOf(
        "Business", "Travel", "Technology", "Science",
        "Culture", "Sports", "Health", "Entertainment"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Progress bar
        LinearProgressIndicator(
            progress = { (currentStep + 1).toFloat() / totalSteps },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Bước ${currentStep + 1} / $totalSteps",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Step content
        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                slideInHorizontally { it } + fadeIn() togetherWith
                        slideOutHorizontally { -it } + fadeOut()
            },
            label = "onboarding_step"
        ) { step ->
            when (step) {
                0 -> StepDisplayName(
                    value = uiState.displayName,
                    onValueChange = viewModel::updateDisplayName
                )
                1 -> StepNativeLanguage(
                    value = uiState.nativeLanguage,
                    onValueChange = viewModel::updateNativeLanguage
                )
                2 -> StepLevel(
                    selected = uiState.level,
                    onSelect = viewModel::updateLevel
                )
                3 -> StepGoals(
                    selected = uiState.goals,
                    onToggle = viewModel::toggleGoal
                )
                4 -> StepTopics(
                    topics = topics,
                    selected = uiState.topics,
                    onToggle = viewModel::toggleTopic
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (currentStep > 0) {
                OutlinedButton(
                    onClick = { currentStep-- },
                    modifier = Modifier.weight(1f)
                ) { Text("Quay lại") }
            }
            Button(
                onClick = {
                    if (currentStep < totalSteps - 1) {
                        currentStep++
                    } else {
                        viewModel.saveProfile(onSuccess = onFinished)
                    }
                },
                enabled = !uiState.isSaving,
                modifier = Modifier.weight(1f)
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text(if (currentStep < totalSteps - 1) "Tiếp theo" else "Bắt đầu!")
                }
            }
        }
    }
}

// --- Step composables ---

@Composable
fun StepDisplayName(value: String, onValueChange: (String) -> Unit) {
    Column {
        Text("Bạn muốn được gọi là gì?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Tên sẽ hiển thị trên màn hình chính", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("Tên hiển thị") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun StepNativeLanguage(value: String, onValueChange: (String) -> Unit) {
    val languages = listOf("Tiếng Việt", "Tiếng Trung", "Tiếng Nhật", "Tiếng Hàn", "Khác")
    Column {
        Text("Ngôn ngữ mẹ đẻ của bạn?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        languages.forEach { lang ->
            OutlinedButton(
                onClick = { onValueChange(lang) },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (value == lang)
                        MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) { Text(lang) }
        }
    }
}

@Composable
fun StepLevel(selected: EnglishLevel?, onSelect: (EnglishLevel) -> Unit) {
    Column {
        Text("Trình độ tiếng Anh hiện tại?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        EnglishLevel.entries.forEach { level ->
            OutlinedButton(
                onClick = { onSelect(level) },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selected == level)
                        MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) { Text(level.label) }
        }
    }
}

@Composable
fun StepGoals(selected: List<LearningGoal>, onToggle: (LearningGoal) -> Unit) {
    Column {
        Text("Mục tiêu học tiếng Anh?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Có thể chọn nhiều", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(24.dp))
        LearningGoal.entries.forEach { goal ->
            OutlinedButton(
                onClick = { onToggle(goal) },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (goal in selected)
                        MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) { Text(goal.label) }
        }
    }
}

@Composable
fun StepTopics(topics: List<String>, selected: List<String>, onToggle: (String) -> Unit) {
    Column {
        Text("Chủ đề bạn quan tâm?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Chọn ít nhất 1 chủ đề", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(24.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            topics.forEach { topic ->
                FilterChip(
                    selected = topic in selected,
                    onClick = { onToggle(topic) },
                    label = { Text(topic) }
                )
            }
        }
    }
}