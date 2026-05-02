package com.example.voicemind.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voicemind.domain.model.EnglishLevel
import com.example.voicemind.domain.model.LearningGoal
import com.example.voicemind.domain.model.UserProfile
import com.example.voicemind.domain.repository.AuthRepository
import com.example.voicemind.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val uid = authRepository.currentUser?.uid
            _startDestination.value = when {
                uid == null -> "login"
                userProfileRepository.isOnboardingCompleted(uid) -> "home"
                else -> "onboarding"
            }
        }
    }

    fun resolveDestinationAfterAuth() {
        viewModelScope.launch {
            val uid = authRepository.currentUser?.uid ?: return@launch
            val completed = userProfileRepository.isOnboardingCompleted(uid)
            _startDestination.value = if (completed) "home" else "onboarding"
        }
    }

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun updateDisplayName(name: String) {
        _uiState.update { it.copy(displayName = name) }
    }

    fun updateNativeLanguage(lang: String) {
        _uiState.update { it.copy(nativeLanguage = lang) }
    }

    fun updateLevel(level: EnglishLevel) {
        _uiState.update { it.copy(level = level) }
    }

    fun toggleGoal(goal: LearningGoal) {
        val current = _uiState.value.goals.toMutableList()
        if (goal in current) current.remove(goal) else current.add(goal)
        _uiState.update { it.copy(goals = current) }
    }

    fun toggleTopic(topic: String) {
        val current = _uiState.value.topics.toMutableList()
        if (topic in current) current.remove(topic) else current.add(topic)
        _uiState.update { it.copy(topics = current) }
    }

    fun saveProfile(onSuccess: () -> Unit) {
        val uid = authRepository.currentUser?.uid ?: return
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val profile = UserProfile(
                uid = uid,
                displayName = state.displayName,
                nativeLanguage = state.nativeLanguage,
                level = state.level ?: EnglishLevel.BEGINNER,
                goals = state.goals,
                topics = state.topics
            )
            userProfileRepository.saveProfile(profile)
                .onSuccess { onSuccess() }
                .onFailure { _uiState.update { it.copy(isSaving = false, error = it.error) } }
        }
    }
}

data class OnboardingUiState(
    val displayName: String = "",
    val nativeLanguage: String = "",
    val level: EnglishLevel? = null,
    val goals: List<LearningGoal> = emptyList(),
    val topics: List<String> = emptyList(),
    val isSaving: Boolean = false,
    val error: String? = null
)