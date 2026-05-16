package com.example.voicemind.ui.screens.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voicemind.domain.model.ForgettingStats
import com.example.voicemind.domain.repository.AuthRepository
import com.example.voicemind.domain.repository.WordProgressRepository
import com.example.voicemind.utils.ReminderScheduler
import com.example.voicemind.utils.StreakManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val wordProgressRepo: WordProgressRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val streakManager = StreakManager(context)
    private val reminderScheduler = ReminderScheduler(context)

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    private val _userAvatarUrl = MutableStateFlow<String?>(null)
    val userAvatarUrl: StateFlow<String?> = _userAvatarUrl.asStateFlow()

    private val _forgettingStats = MutableStateFlow(ForgettingStats())
    val forgettingStats: StateFlow<ForgettingStats> = _forgettingStats.asStateFlow()

    private val _streak = MutableStateFlow(0)
    val streak: StateFlow<Int> = _streak.asStateFlow()

    private val _reminderEnabled = MutableStateFlow(false)
    val reminderEnabled: StateFlow<Boolean> = _reminderEnabled.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadUserData()
        loadLearningStats()
        loadStreak()
        loadReminderPreference()
    }

    private fun loadUserData() {
        val currentUser = authRepository.currentUser
        _userName.value = currentUser?.displayName ?: "Người dùng"
        _userEmail.value = currentUser?.email ?: ""
        _userAvatarUrl.value = currentUser?.photoUrl?.toString()
    }

    private fun loadLearningStats() {
        viewModelScope.launch {
            _isLoading.value = true
            val statsMap = wordProgressRepo.getStats() // trả về Map<Int,Int>
            _forgettingStats.value = ForgettingStats(
                level1 = statsMap[1] ?: 0,
                level2 = statsMap[2] ?: 0,
                level3 = statsMap[3] ?: 0,
                level4 = statsMap[4] ?: 0,
                level5 = statsMap[5] ?: 0,
                mastered = statsMap[6] ?: 0
            )
            _isLoading.value = false
        }
    }

    private fun loadStreak() {
        _streak.value = streakManager.currentStreak.value
    }

    private fun loadReminderPreference() {
        val prefs = context.getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE)
        _reminderEnabled.value = prefs.getBoolean("reminder_enabled", false)
        if (_reminderEnabled.value) {
            reminderScheduler.scheduleDailyReminder()
        } else {
            reminderScheduler.cancelReminder()
        }
    }

    fun toggleReminder(enabled: Boolean) {
        _reminderEnabled.value = enabled
        val prefs = context.getSharedPreferences("reminder_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("reminder_enabled", enabled).apply()
        if (enabled) {
            reminderScheduler.scheduleDailyReminder()
        } else {
            reminderScheduler.cancelReminder()
        }
    }

    fun signOut(onSignOut: () -> Unit) {
        viewModelScope.launch {
            authRepository.signOut()
            onSignOut()
        }
    }
}