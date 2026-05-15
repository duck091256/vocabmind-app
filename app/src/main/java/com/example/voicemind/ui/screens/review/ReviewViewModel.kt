package com.example.voicemind.ui.screens.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voicemind.domain.model.WordProgress
import com.example.voicemind.domain.repository.WordProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val repository: WordProgressRepository
) : ViewModel() {

    private val _wordsToReview = MutableStateFlow<List<WordProgress>>(emptyList())
    val wordsToReview: StateFlow<List<WordProgress>> = _wordsToReview.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _definition = MutableStateFlow("")
    val definition: StateFlow<String> = _definition.asStateFlow()

    private val _feedback = MutableStateFlow<String?>(null)
    val feedback: StateFlow<String?> = _feedback.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadReviewWords()
    }

    fun loadReviewWords() {
        viewModelScope.launch {
            _isLoading.value = true
            val words = repository.getWordsNeedReview()
            _wordsToReview.value = words
            if (words.isNotEmpty()) {
                loadDefinition(words[0].word)
            }
            _isLoading.value = false
        }
    }

    private fun loadDefinition(word: String) {
        viewModelScope.launch {
            val def = repository.getDefinition(word)
            _definition.value = def ?: "Không tìm thấy định nghĩa"
        }
    }

    fun submitAnswer(userAnswer: String, onComplete: () -> Unit) {
        val currentWord = _wordsToReview.value.getOrNull(_currentIndex.value) ?: return
        val isCorrect = userAnswer.trim().lowercase() == currentWord.word.lowercase()
        viewModelScope.launch {
            repository.updateReview(currentWord.word, isCorrect)
            _feedback.value = if (isCorrect) "✅ Chính xác! Đã cập nhật tiến độ." else "❌ Sai. Đáp án: ${currentWord.word}. Học lại từ đầu."
            // Sau 1.5s, chuyển tiếp hoặc tải lại danh sách
            kotlinx.coroutines.delay(1500)
            _feedback.value = null
            // Tải lại danh sách (có thể từ vừa ôn đã được nâng level, không còn trong danh sách cần ôn)
            loadReviewWords()
            // Reset index về 0
            _currentIndex.value = 0
            onComplete()
        }
    }

    fun nextWord() {
        if (_currentIndex.value + 1 < _wordsToReview.value.size) {
            _currentIndex.value++
            loadDefinition(_wordsToReview.value[_currentIndex.value].word)
        } else {
            // Hết danh sách, tải lại (có thể còn từ mới)
            loadReviewWords()
            _currentIndex.value = 0
        }
    }
}