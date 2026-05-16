package com.example.voicemind.domain.game

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class GameMode { PLAYER_TURN, AI_TURN, GAME_OVER }
enum class Difficulty { EASY, MEDIUM, HARD }

class WordChainGame(
    private val rawWordSet: Set<String>,
    private val difficulty: Difficulty = Difficulty.MEDIUM

) {
    private var wordSet: Set<String> = rawWordSet.filter {
        it.matches(Regex("^[a-z]+$")) && it.length >= 2
    }.toSet()

    init {
        if (wordSet.isEmpty()) {
            // Fallback nếu không có từ hợp lệ
            wordSet = setOf("apple", "elephant", "tiger", "rabbit", "table")
        }
    }
    private val _currentWord = MutableStateFlow("")
    val currentWord: StateFlow<String> = _currentWord.asStateFlow()

    private val _playerScore = MutableStateFlow(0)
    val playerScore: StateFlow<Int> = _playerScore.asStateFlow()

    private val _aiScore = MutableStateFlow(0)
    val aiScore: StateFlow<Int> = _aiScore.asStateFlow()

    private val _chain = MutableStateFlow<List<String>>(emptyList())
    val chain: StateFlow<List<String>> = _chain.asStateFlow()

    private val _gameMode = MutableStateFlow(GameMode.PLAYER_TURN)
    val gameMode: StateFlow<GameMode> = _gameMode.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    private val _aiLastWord = MutableStateFlow<String?>(null)
    val aiLastWord: StateFlow<String?> = _aiLastWord.asStateFlow()

    private val _comboMultiplier = MutableStateFlow(1)
    val comboMultiplier: StateFlow<Int> = _comboMultiplier.asStateFlow()


    // ✅ HÀM START GAME (đã thêm)
    fun startGame(startWord: String) {
        val word = startWord.lowercase().trim()
        require(wordSet.contains(word)) { "Từ bắt đầu không hợp lệ" }
        _currentWord.value = word
        _chain.value = listOf(word)
        _playerScore.value = 0
        _aiScore.value = 0
        _gameMode.value = GameMode.PLAYER_TURN
        _message.value = null
        _aiLastWord.value = null
        _comboMultiplier.value = 1
    }

    fun submitWord(word: String): Boolean {
        if (_gameMode.value != GameMode.PLAYER_TURN) {
            _message.value = "Chưa tới lượt bạn!"
            return false
        }
        val cleanWord = word.lowercase().trim()
        if (cleanWord.isEmpty()) {
            _message.value = "Vui lòng nhập từ"
            return false
        }
        if (cleanWord.first() != _currentWord.value.last()) {
            _message.value = "Từ phải bắt đầu bằng chữ '${_currentWord.value.last().uppercase()}'"
            return false
        }
        if (_chain.value.contains(cleanWord)) {
            _message.value = "Từ này đã được dùng rồi"
            return false
        }
        if (!wordSet.contains(cleanWord)) {
            _message.value = "Từ không có trong danh sách từ vựng"
            return false
        }

        val points = cleanWord.length
        _playerScore.value += points
        _message.value = "✅ +$points điểm"
        _chain.value = _chain.value + cleanWord
        _currentWord.value = cleanWord
        _gameMode.value = GameMode.AI_TURN
        return true
    }

    suspend fun submitAIMove(): Boolean {
        if (_gameMode.value != GameMode.AI_TURN) return false
        val lastChar = _currentWord.value.last()
        val candidates = wordSet.filter { it.startsWith(lastChar) && !_chain.value.contains(it) }
        val aiWord = when (difficulty) {
            Difficulty.EASY -> candidates.randomOrNull()
            Difficulty.MEDIUM -> candidates.maxByOrNull { it.length }
            Difficulty.HARD -> candidates.maxByOrNull { word ->
                wordSet.count { it.startsWith(word.last()) && !_chain.value.contains(it) }
            }
        }
        if (aiWord != null) {
            val points = aiWord.length
            _aiScore.value += points
            _message.value = "🤖 AI chọn: ${aiWord.uppercase()} (+$points)"
            _aiLastWord.value = aiWord
            _chain.value = _chain.value + aiWord
            _currentWord.value = aiWord
            _gameMode.value = GameMode.PLAYER_TURN
            return true
        } else {
            _gameMode.value = GameMode.GAME_OVER
            _message.value = "AI không có nước đi! Bạn thắng!"
            return false
        }
    }

    fun getCurrentWordSet(): Set<String> = wordSet
}