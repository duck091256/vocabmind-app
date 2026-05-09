package com.example.voicemind.ui.screens.sets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voicemind.domain.usecase.GetWordsBySetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.collections.emptyList

@HiltViewModel
class SetDetailViewModel @Inject constructor(
    private val getWords: GetWordsBySetUseCase
) : ViewModel() {

    fun getWords(setId: String) =
        getWords.invoke(setId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList<String>())
}