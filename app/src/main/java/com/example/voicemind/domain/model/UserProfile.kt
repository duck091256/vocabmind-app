package com.example.voicemind.domain.model

data class UserProfile(
    val uid: String,
    val displayName: String,
    val nativeLanguage: String,
    val level: EnglishLevel,
    val goals: List<LearningGoal>,
    val topics: List<String>,
    val onboardingCompleted: Boolean = true
)

enum class EnglishLevel(val label: String) {
    BEGINNER("Beginner (A1-A2)"),
    INTERMEDIATE("Intermediate (B1-B2)"),
    ADVANCED("Advanced (C1-C2)")
}

enum class LearningGoal(val label: String) {
    WORK("Công việc"),
    STUDY("Học tập / Thi cử"),
    TRAVEL("Du lịch"),
    DAILY("Giao tiếp hàng ngày"),
    OTHER("Khác")
}