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

@HiltViewModel
class CreateSetViewModel @Inject constructor(
    private val vocabRepository: VocabRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<CreateSetUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun create(
        title: String,
        description: String,
        words: List<Pair<String, String>>
    ) {
        viewModelScope.launch {
            if (title.isBlank()) {
                _uiEvent.emit(CreateSetUiEvent.ShowSnackbar("Title cannot be empty"))
                return@launch
            }
            
            val userId = authRepository.currentUser?.uid ?: return@launch
            
            when (val result = vocabRepository.createSet(title, description, words, userId)) {
                is Resource.Success -> {
                    _uiEvent.emit(CreateSetUiEvent.Success)
                }
                is Resource.Error -> {
                    _uiEvent.emit(CreateSetUiEvent.ShowSnackbar(result.message ?: "Failed to create set"))
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