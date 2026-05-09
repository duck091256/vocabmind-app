package com.example.voicemind.ui.screens.sets

import com.example.voicemind.domain.model.VocabSet

data class SetsUiState(
    val isLoading: Boolean = true,
    val sets: List<VocabSet> = emptyList(),
    val error: String? = null,
    val searchQuery: String = "",
    val sortOption: SortOption = SortOption.NEWEST
)

enum class SortOption {
    NEWEST,
    OLDEST,
    ALPHABETICAL,
    MOST_WORDS
}

sealed interface SetsUiAction {
    data class SearchQueryChanged(val query: String) : SetsUiAction
    data class SortOptionChanged(val option: SortOption) : SetsUiAction
    data class CreateSet(val title: String, val description: String, val totalWords: Int) : SetsUiAction
    data class DeleteSet(val setId: String) : SetsUiAction
    object RefreshSets : SetsUiAction
}

sealed interface SetsUiEvent {
    data class ShowSnackbar(val message: String) : SetsUiEvent
    data class NavigateToDetail(val setId: String) : SetsUiEvent
}
