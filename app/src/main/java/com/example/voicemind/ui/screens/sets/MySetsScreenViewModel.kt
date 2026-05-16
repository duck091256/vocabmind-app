package com.example.voicemind.ui.screens.sets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voicemind.domain.model.Resource
import com.example.voicemind.domain.repository.AuthRepository
import com.example.voicemind.domain.repository.VocabRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.example.voicemind.domain.model.ForgettingStats
import com.example.voicemind.domain.repository.WordProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow

@OptIn(FlowPreview::class)
@HiltViewModel
class MySetsViewModel @Inject constructor(
    private val vocabRepository: VocabRepository,
    private val authRepository: AuthRepository,
    private val wordProgressRepo: WordProgressRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _sortOption = MutableStateFlow(SortOption.NEWEST)
    
    private val _forgettingStats = MutableStateFlow<ForgettingStats?>(null)
    val forgettingStats: StateFlow<ForgettingStats?> = _forgettingStats.asStateFlow()

    private val _vocabularyState = MutableStateFlow(VocabularyState(0, 0, false))
    val vocabularyState: StateFlow<VocabularyState> = _vocabularyState.asStateFlow()

    private val _totalWords = MutableStateFlow(0)
    val totalWords: StateFlow<Int> = _totalWords.asStateFlow()

    private val _learnedWords = MutableStateFlow(0)
    val learnedWords: StateFlow<Int> = _learnedWords.asStateFlow()
    
    private val _uiEvent = Channel<SetsUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val setsFlow = vocabRepository.getAllSets()

    val uiState: StateFlow<SetsUiState> = combine(
        setsFlow,
        _searchQuery.debounce(300),
        _sortOption
    ) { resource, query, sortOpt ->
        when (resource) {
            is Resource.Loading -> SetsUiState(isLoading = true, searchQuery = query, sortOption = sortOpt)
            is Resource.Error -> SetsUiState(isLoading = false, error = resource.message, searchQuery = query, sortOption = sortOpt)
            is Resource.Success -> {
                val filtered = resource.data.filter {
                    it.title.contains(query, ignoreCase = true) || 
                    it.description.contains(query, ignoreCase = true)
                }
                val sorted = when (sortOpt) {
                    SortOption.NEWEST -> filtered.sortedByDescending { it.createdAt }
                    SortOption.OLDEST -> filtered.sortedBy { it.createdAt }
                    SortOption.ALPHABETICAL -> filtered.sortedBy { it.title.lowercase() }
                    SortOption.MOST_WORDS -> filtered.sortedByDescending { it.totalWords }
                }
                SetsUiState(
                    isLoading = false,
                    sets = sorted,
                    searchQuery = query,
                    sortOption = sortOpt
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SetsUiState(isLoading = true)
    )

    init {
        // Trigger initial sync if needed
        syncData()
    }

    private fun syncData() {
        viewModelScope.launch {
            authRepository.currentUser?.uid?.let { userId ->
                vocabRepository.syncSetsForUser(userId)
            }
            loadGlobalStats()
        }
    }

    private fun loadGlobalStats() {
        viewModelScope.launch {
            val statsMap = wordProgressRepo.getStats()
            _forgettingStats.value = ForgettingStats(
                level1 = statsMap[1] ?: 0,
                level2 = statsMap[2] ?: 0,
                level3 = statsMap[3] ?: 0,
                level4 = statsMap[4] ?: 0,
                level5 = statsMap[5] ?: 0,
                mastered = statsMap[6] ?: 0
            )

            val learned = statsMap.values.sum()
            _learnedWords.value = learned

            val reviewWords = wordProgressRepo.getWordsNeedReview()
            val needReview = reviewWords.isNotEmpty()
            val reviewCount = reviewWords.size

            _vocabularyState.value = VocabularyState(
                learnedWords = learned,
                totalWords = _totalWords.value,
                wordsToReviewCount = reviewCount,
                hasWordsToReview = needReview,
                reviewAfterDays = 0 // Global, can be ignored or calculated if needed
            )
        }
        
        viewModelScope.launch {
            vocabRepository.getAllSets().collect { resource ->
                if (resource is Resource.Success) {
                    val total = resource.data.sumOf { it.totalWords }
                    _totalWords.value = total
                }
            }
        }
    }

    fun onAction(action: SetsUiAction) {
        when (action) {
            is SetsUiAction.SearchQueryChanged -> {
                _searchQuery.value = action.query
            }
            is SetsUiAction.SortOptionChanged -> {
                _sortOption.value = action.option
            }
            is SetsUiAction.CreateSet -> {
                createSet(action.title, action.description, emptyList())
            }
            is SetsUiAction.DeleteSet -> {
                deleteSet(action.setId)
            }
            is SetsUiAction.RefreshSets -> {
                syncData()
            }
        }
    }

    private fun createSet(title: String, description: String, words: List<Pair<String, String>>) {
        viewModelScope.launch {
            val userId = authRepository.currentUser?.uid ?: return@launch
            when (val result = vocabRepository.createSet(title, description, words, userId)) {
                is Resource.Success -> {
                    _uiEvent.send(SetsUiEvent.ShowSnackbar("Set created successfully"))
                }
                is Resource.Error -> {
                    _uiEvent.send(SetsUiEvent.ShowSnackbar(result.message ?: "Failed to create set"))
                }
                is Resource.Loading -> {}
            }
        }
    }

    private fun deleteSet(setId: String) {
        viewModelScope.launch {
            when (val result = vocabRepository.deleteSet(setId)) {
                is Resource.Success -> {
                    _uiEvent.send(SetsUiEvent.ShowSnackbar("Set deleted successfully"))
                }
                is Resource.Error -> {
                    _uiEvent.send(SetsUiEvent.ShowSnackbar(result.message ?: "Failed to delete set"))
                }
                is Resource.Loading -> {}
            }
        }
    }
}