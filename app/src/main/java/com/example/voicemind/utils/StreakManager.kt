package com.example.voicemind.utils

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*

class StreakManager(private val context: Context) {

    private val prefs = context.getSharedPreferences("streak_prefs", Context.MODE_PRIVATE)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private val _currentStreak = MutableStateFlow(0)
    val currentStreak: StateFlow<Int> = _currentStreak

    init {
        loadStreak()
    }

    private fun loadStreak() {
        val lastDate = prefs.getString("last_study_date", null)
        val streak = prefs.getInt("streak_count", 0)
        _currentStreak.value = streak
    }

    fun updateStreakIfNeeded() {
        val today = dateFormat.format(Date())
        val lastDate = prefs.getString("last_study_date", null)

        if (lastDate == today) return

        val streak = if (lastDate == null) {
            1
        } else {
            val last = dateFormat.parse(lastDate) ?: return
            val yesterday = Calendar.getInstance().apply {
                time = Date()
                add(Calendar.DAY_OF_YEAR, -1)
            }.time
            if (dateFormat.format(last) == dateFormat.format(yesterday)) {
                prefs.getInt("streak_count", 0) + 1
            } else {
                1
            }
        }

        prefs.edit().putString("last_study_date", today).putInt("streak_count", streak).apply()
        _currentStreak.value = streak
    }
}