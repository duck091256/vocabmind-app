package com.example.voicemind.ui.screens.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voicemind.domain.repository.AuthRepository
import com.example.voicemind.domain.repository.UserProfileRepository
import com.example.voicemind.domain.repository.WordProgressRepository
import com.example.voicemind.utils.StreakManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeDashboardViewModel @Inject constructor(
    private val repository: UserProfileRepository,
    private val authRepository: AuthRepository,
    private val wordProgressRepo: WordProgressRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val streakManager = StreakManager(context)

    private val _forgettingStats = MutableStateFlow(ForgettingStats())
    val forgettingStats: StateFlow<ForgettingStats> = _forgettingStats.asStateFlow()

    private val _reviewCount = MutableStateFlow(0)
    val reviewCount: StateFlow<Int> = _reviewCount.asStateFlow()

    private val _displayName = MutableStateFlow("")
    val displayName: StateFlow<String> = _displayName.asStateFlow()

    private val _streak = MutableStateFlow(0)
    val streak: StateFlow<Int> = _streak.asStateFlow()

    init {
        loadStats()
        loadUserDisplayName()
        loadStreak()
    }

    private fun loadStreak() {
        _streak.value = streakManager.currentStreak.value
    }

    fun loadStats() {
        viewModelScope.launch {
            val stats = wordProgressRepo.getStats()
            _forgettingStats.value = ForgettingStats(
                after1Hour = stats[1] ?: 0,
                after1Day = stats[2] ?: 0,
                after3Days = stats[3] ?: 0,
                after1Week = stats[4] ?: 0,
                permanent = stats[5] ?: 0
            )
            val needReview = wordProgressRepo.getWordsNeedReview().size
            _reviewCount.value = needReview
            // Cập nhật streak dựa trên hoạt động học (nếu có học từ mới trong phiên này, streakManager đã tự cập nhật)
            loadStreak()
        }
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