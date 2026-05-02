package com.example.voicemind.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voicemind.domain.repository.AuthRepository
import com.example.voicemind.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeDashboardViewModel @Inject constructor(
    private val repository: UserProfileRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _displayName = MutableStateFlow("")
    val displayName: StateFlow<String> = _displayName

    init {
        loadUserDisplayName()
    }
    private fun loadUserDisplayName() {
        viewModelScope.launch {
            val uid = authRepository.currentUser?.uid
            if (uid != null) {
                val name = repository.getDisplayName(uid)
                _displayName.value = name ?: "User"
            } else {
                _displayName.value = "Guest"
            }
        }
    }
}