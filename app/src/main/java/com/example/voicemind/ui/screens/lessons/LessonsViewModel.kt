package com.example.voicemind.ui.screens.lessons

import android.content.Context
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voicemind.domain.model.Lesson
import com.example.voicemind.domain.repository.LessonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonsViewModel @Inject constructor(
    private val repository: LessonRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _lessons = MutableStateFlow<List<Lesson>>(emptyList())
    val lessons: StateFlow<List<Lesson>> = _lessons.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // SharedPreferences lưu trạng thái hoàn thành bài học
    private val prefs = context.getSharedPreferences("lesson_progress", Context.MODE_PRIVATE)
    private val _completedLessons = mutableStateMapOf<String, Boolean>()

    init {
        loadLessons()
        loadCompletedStatus()
    }

    private fun loadLessons() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getAllLessons()
            _lessons.value = result
            // Sau khi có danh sách bài, cập nhật trạng thái hoàn thành
            loadCompletedStatus()
            _isLoading.value = false
        }
    }

    private fun loadCompletedStatus() {
        _lessons.value.forEach { lesson ->
            val isCompleted = prefs.getBoolean("lesson_${lesson.id}", false)
            _completedLessons[lesson.id] = isCompleted
        }
    }

    fun isLessonCompleted(lessonId: String): Boolean {
        return _completedLessons[lessonId] ?: false
    }

    fun isLessonUnlocked(lessonId: String): Boolean {
        val lessonsList = _lessons.value
        val index = lessonsList.indexOfFirst { it.id == lessonId }
        if (index == -1) return false
        // Bài đầu tiên luôn mở khóa
        if (index == 0) return true
        // Kiểm tra bài trước đã hoàn thành chưa
        val previousLesson = lessonsList[index - 1]
        return isLessonCompleted(previousLesson.id)
    }

    fun markLessonCompleted(lessonId: String) {
        if (!isLessonCompleted(lessonId)) {
            _completedLessons[lessonId] = true
            prefs.edit().putBoolean("lesson_${lessonId}", true).apply()
        }
    }

    fun completeLesson(lessonId: String) {
        markLessonCompleted(lessonId)
    }

    fun refreshCompletedStatus() {
        loadCompletedStatus()
    }
}