package com.example.voicemind.domain.repository

import com.example.voicemind.domain.model.UserProfile

interface UserProfileRepository {
    suspend fun saveProfile(profile: UserProfile): Result<Unit>
    suspend fun getProfile(uid: String): UserProfile?
    suspend fun isOnboardingCompleted(uid: String): Boolean
}