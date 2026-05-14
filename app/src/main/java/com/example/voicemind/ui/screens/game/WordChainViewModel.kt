package com.example.voicemind.ui.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voicemind.domain.game.WordChainGame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordChainViewModel @Inject constructor() : ViewModel() {

    private val game = WordChainGame()

    val currentWord = game.currentWord
    val score = game.score
    val chain = game.chain
    val isGameOver = game.isGameOver
    val message = game.message

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // Giả lập load dữ liệu (nếu cần lấy từ xa, thay bằng repository)
        viewModelScope.launch {
            _isLoading.value = true
            delay(300) // chỉ để mô phỏng, có thể bỏ
            // Có thể cập nhật từ điển ở đây nếu muốn mở rộng
            _isLoading.value = false
            startNewGame()
        }
    }

    fun submitWord(word: String): Boolean {
        return game.submitWord(word)
    }

    fun endGame() {
        game.endGame()
    }

    fun startNewGame() {
        val startingWords = listOf("apple", "elephant", "tiger", "rabbit", "table", "eagle", "great")
        val randomWord = startingWords.random()
        game.startGame(randomWord)
    }

    fun resetGame() {
        startNewGame()
    }
}
