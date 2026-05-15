package com.example.voicemind.ui.screens.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voicemind.domain.game.Difficulty
import com.example.voicemind.domain.game.GameMode
import com.example.voicemind.domain.game.WordChainGame
import com.example.voicemind.domain.repository.VocabularyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordChainViewModel @Inject constructor(
    private val vocabularyRepo: VocabularyRepository
) : ViewModel() {

    private var game: WordChainGame? = null

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Các state từ game
    private val _currentWord = MutableStateFlow("")
    val currentWord: StateFlow<String> = _currentWord.asStateFlow()

    private val _playerScore = MutableStateFlow(0)
    val playerScore: StateFlow<Int> = _playerScore.asStateFlow()

    private val _aiScore = MutableStateFlow(0)
    val aiScore: StateFlow<Int> = _aiScore.asStateFlow()

    private val _chain = MutableStateFlow<List<String>>(emptyList())
    val chain: StateFlow<List<String>> = _chain.asStateFlow()

    private val _gameMode = MutableStateFlow(GameMode.GAME_OVER)
    val gameMode: StateFlow<GameMode> = _gameMode.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    private val _aiLastWord = MutableStateFlow<String?>(null)
    val aiLastWord: StateFlow<String?> = _aiLastWord.asStateFlow()

    private val _comboMultiplier = MutableStateFlow(1)
    val comboMultiplier: StateFlow<Int> = _comboMultiplier.asStateFlow()

    private val _currentDifficulty = MutableStateFlow(Difficulty.MEDIUM)
    val currentDifficulty: StateFlow<Difficulty> = _currentDifficulty.asStateFlow()

    init {
        loadGame()
    }

    private fun loadGame() {
        viewModelScope.launch {
            _isLoading.value = true
            val words = vocabularyRepo.getWordSet()
            if (words.isNotEmpty()) {
                Log.d("WordChainVM", "Loaded ${words.size} words")
                game = WordChainGame(words, _currentDifficulty.value)
                startNewGame()
                // Lắng nghe các thay đổi từ game
                listenToGame()
            } else {
                Log.e("WordChainVM", "Word set is empty!")
            }
            _isLoading.value = false
        }
    }

    private fun listenToGame() {
        viewModelScope.launch {
            game?.currentWord?.collect { _currentWord.value = it }
        }
        viewModelScope.launch {
            game?.playerScore?.collect { _playerScore.value = it }
        }
        viewModelScope.launch {
            game?.aiScore?.collect { _aiScore.value = it }
        }
        viewModelScope.launch {
            game?.chain?.collect { _chain.value = it }
        }
        viewModelScope.launch {
            game?.gameMode?.collect { _gameMode.value = it }
        }
        viewModelScope.launch {
            game?.message?.collect { _message.value = it }
        }
        viewModelScope.launch {
            game?.aiLastWord?.collect { _aiLastWord.value = it }
        }
        viewModelScope.launch {
            game?.comboMultiplier?.collect { _comboMultiplier.value = it }
        }
    }

    fun startNewGame() {
        game?.let {
            val startWord = selectRandomWord()
            it.startGame(startWord)
        }
    }

    private fun selectRandomWord(): String {
        return listOf("apple", "elephant", "table", "energy", "yellow").random()
    }

    fun submitWord(word: String) {
        viewModelScope.launch {
            if (game?.submitWord(word) == true) {
                delay(800)
                game?.submitAIMove()
            }
        }
    }

    fun setDifficulty(difficulty: Difficulty) {
        _currentDifficulty.value = difficulty
        restartGameWithNewDifficulty()
    }

    private fun restartGameWithNewDifficulty() {
        viewModelScope.launch {
            val currentWordSet = getCurrentWordSet()
            if (currentWordSet.isNotEmpty()) {
                game = WordChainGame(currentWordSet, _currentDifficulty.value)
                startNewGame()
                listenToGame()
            }
        }
    }

    private fun getCurrentWordSet(): Set<String> = game?.getCurrentWordSet() ?: emptySet()
}