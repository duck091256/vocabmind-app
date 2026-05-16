package com.example.voicemind.ui.screens.sets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voicemind.domain.model.ForgettingStats
import com.example.voicemind.domain.model.Resource
import com.example.voicemind.domain.model.Word
import com.example.voicemind.domain.repository.WordProgressRepository
import com.example.voicemind.domain.usecase.GetWordsBySetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

import androidx.lifecycle.SavedStateHandle
import com.example.voicemind.domain.model.VocabSet
import com.example.voicemind.domain.usecase.GetSetByIdUseCase
import com.example.voicemind.domain.model.WordProgress

@HiltViewModel
class SetDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getWordsUseCase: GetWordsBySetUseCase,
    private val getSetByIdUseCase: GetSetByIdUseCase,
    private val wordProgressRepo: WordProgressRepository
) : ViewModel() {

    val setId: String = savedStateHandle.get<String>("setId") ?: ""

    private val _forgettingStats = MutableStateFlow<ForgettingStats?>(null)
    val forgettingStats: StateFlow<ForgettingStats?> = _forgettingStats.asStateFlow()

    private val _wordProgressMap = MutableStateFlow<Map<String, WordProgress>>(emptyMap())
    val wordProgressMap: StateFlow<Map<String, WordProgress>> = _wordProgressMap.asStateFlow()

    val wordsResource: StateFlow<Resource<List<Word>>> = getWordsUseCase.invoke(setId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Resource.Loading)

    val setResource: StateFlow<Resource<VocabSet>> = getSetByIdUseCase.invoke(setId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Resource.Loading)

    fun loadStatsForSet(words: List<Word>) {
        viewModelScope.launch {
            val statsMap = wordProgressRepo.getStatsForWords(words.map { it.word })
            _forgettingStats.value = ForgettingStats(
                level1 = statsMap[1] ?: 0,
                level2 = statsMap[2] ?: 0,
                level3 = statsMap[3] ?: 0,
                level4 = statsMap[4] ?: 0,
                level5 = statsMap[5] ?: 0,
                mastered = statsMap[6] ?: 0
            )
            
            val progressMap = wordProgressRepo.getWordProgressMap(words.map { it.word })
            _wordProgressMap.value = progressMap
        }
    }
}