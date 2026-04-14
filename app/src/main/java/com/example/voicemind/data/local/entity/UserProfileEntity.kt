package com.example.voicemind.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val uid: String,
    val displayName: String,
    val nativeLanguage: String,
    val level: String,
    val goals: String,       // JSON array string
    val topics: String,      // JSON array string
    val onboardingCompleted: Boolean
)