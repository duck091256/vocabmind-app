package com.example.voicemind.ui.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voicemind.domain.game.WordChainGame
import com.example.voicemind.domain.repository.VocabularyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordChainViewModel @Inject constructor(
    private val vocabularyRepo: VocabularyRepository
) : ViewModel() {

    private val game = WordChainGame()

    val currentWord = game.currentWord
    val score = game.score
    val chain = game.chain
    val isGameOver = game.isGameOver
    val message = game.message

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadVocabulary()
    }

    private fun loadVocabulary() {
        viewModelScope.launch {
            _isLoading.value = true
            val wordSet = vocabularyRepo.getWordSet()
            if (wordSet.isNotEmpty()) {
                game.updateDictionary(wordSet)
            }
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
        val currentSet = game.getCurrentWordSet()
        if (currentSet.isNotEmpty()) {
            val startWord = currentSet.random()
            game.startGame(startWord)
        } else {
            // Fallback nếu chưa có dữ liệu
            game.startGame("apple")
        }
    }

    fun resetGame() {
        startNewGame()
    }
}