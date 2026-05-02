package com.example.voicemind.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voicemind.domain.model.User
import com.example.voicemind.domain.repository.AuthRepository
import com.example.voicemind.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    val currentUser get() = authRepository.currentUser

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepository.signInWithEmail(email, password)
                .onSuccess { user ->
                    val uid = user.uid
                    userProfileRepository.syncUser(uid)
                    _authState.value = AuthState.Success(user)
                }
                .onFailure { _authState.value = AuthState.Error(it.message ?: "Lỗi không xác định") }
        }
    }

    fun signUpWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepository.signUpWithEmail(email, password)
                .onSuccess { user ->
                    val uid = user.uid
                    userProfileRepository.syncUser(uid)
                    _authState.value = AuthState.Success(user)
                }
                .onFailure { _authState.value = AuthState.Error(it.message ?: "Lỗi không xác định") }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepository.signInWithGoogle(idToken)
                .onSuccess { user ->
                    val uid = user.uid
                    userProfileRepository.syncUser(uid)
                    _authState.value = AuthState.Success(user)
                }
                .onFailure { _authState.value = AuthState.Error(it.message ?: "Lỗi không xác định") }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _authState.value = AuthState.Idle
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}