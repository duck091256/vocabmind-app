package com.example.voicemind.ui.screens.sets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voicemind.domain.model.Resource
import com.example.voicemind.domain.repository.AuthRepository
import com.example.voicemind.domain.repository.VocabRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import androidx.lifecycle.SavedStateHandle
import com.example.voicemind.domain.model.VocabSet
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest

@HiltViewModel
class CreateSetScreenViewModel @Inject constructor(
    private val vocabRepository: VocabRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<CreateSetUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val setId: String? = savedStateHandle.get<String>("setId")

    private val _existingSet = MutableStateFlow<VocabSet?>(null)
    val existingSet: StateFlow<VocabSet?> = _existingSet.asStateFlow()

    init {
        setId?.let { id ->
            viewModelScope.launch {
                vocabRepository.getSetById(id).collectLatest { resource ->
                    if (resource is Resource.Success) {
                        _existingSet.value = resource.data
                    }
                }
            }
        }
    }

    fun createOrAddWords(
        title: String,
        description: String,
        words: List<Pair<String, String>>
    ) {
        viewModelScope.launch {
            if (words.isEmpty()) {
                _uiEvent.emit(CreateSetUiEvent.ShowSnackbar("Please add at least one word"))
                return@launch
            }
            
            val userId = authRepository.currentUser?.uid ?: return@launch
            
            val result = if (setId != null) {
                vocabRepository.addWordsToSet(setId, words, userId)
            } else {
                if (title.isBlank()) {
                    _uiEvent.emit(CreateSetUiEvent.ShowSnackbar("Title cannot be empty"))
                    return@launch
                }
                vocabRepository.createSet(title, description, words, userId)
            }
            
            when (result) {
                is Resource.Success -> {
                    _uiEvent.emit(CreateSetUiEvent.Success)
                }
                is Resource.Error -> {
                    _uiEvent.emit(CreateSetUiEvent.ShowSnackbar(result.message ?: "Failed to save"))
                }
                is Resource.Loading -> {}
            }
        }
    }
}

sealed class CreateSetUiEvent {
    object Success : CreateSetUiEvent()
    data class ShowSnackbar(val message: String) : CreateSetUiEvent()
}