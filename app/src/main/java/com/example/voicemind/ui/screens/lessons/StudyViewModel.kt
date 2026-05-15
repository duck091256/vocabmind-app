package com.example.voicemind.ui.screens.lessons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voicemind.domain.model.Lesson
import com.example.voicemind.domain.repository.LessonRepository
import com.example.voicemind.domain.repository.WordProgressRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class StudyViewModel @Inject constructor(
    private val repository: LessonRepository,
    private val wordProgressRepo: WordProgressRepository
) : ViewModel() {

    private val _lesson = MutableStateFlow<Lesson?>(null)
    val lesson: StateFlow<Lesson?> = _lesson.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadLesson(lessonId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _lesson.value = repository.getLessonById(lessonId)
            _isLoading.value = false
        }
    }

    fun saveWordProgress(word: String) {
        viewModelScope.launch {
            val cal = Calendar.getInstance()
            cal.add(Calendar.HOUR_OF_DAY, 1)
            val nextReview = Timestamp(cal.time)
            wordProgressRepo.saveProgress(word, 1, nextReview)
        }
    }
}
