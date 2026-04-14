package com.example.voicemind.data.repository

import com.example.voicemind.data.local.dao.UserProfileDao
import com.example.voicemind.data.local.entity.UserProfileEntity
import com.example.voicemind.domain.model.EnglishLevel
import com.example.voicemind.domain.model.LearningGoal
import com.example.voicemind.domain.model.UserProfile
import com.example.voicemind.domain.repository.UserProfileRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserProfileRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val dao: UserProfileDao
) : UserProfileRepository {

    override suspend fun saveProfile(profile: UserProfile): Result<Unit> {
        return try {
            // Lưu Firestore
            val data = mapOf(
                "displayName" to profile.displayName,
                "nativeLanguage" to profile.nativeLanguage,
                "level" to profile.level.name,
                "goals" to profile.goals.map { it.name },
                "topics" to profile.topics,
                "onboardingCompleted" to true
            )
            firestore.collection("users")
                .document(profile.uid)
                .set(data)
                .await()

            // Lưu Room
            dao.insertOrUpdate(profile.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProfile(uid: String): UserProfile? {
        return dao.getByUid(uid)?.toDomain()
    }

    override suspend fun isOnboardingCompleted(uid: String): Boolean {
        return dao.getByUid(uid)?.onboardingCompleted == true
    }

    private fun UserProfile.toEntity() = UserProfileEntity(
        uid = uid,
        displayName = displayName,
        nativeLanguage = nativeLanguage,
        level = level.name,
        goals = goals.joinToString(",") { it.name },
        topics = topics.joinToString(","),
        onboardingCompleted = onboardingCompleted
    )

    private fun UserProfileEntity.toDomain() = UserProfile(
        uid = uid,
        displayName = displayName,
        nativeLanguage = nativeLanguage,
        level = EnglishLevel.valueOf(level),
        goals = goals.split(",").filter { it.isNotBlank() }.map { LearningGoal.valueOf(it) },
        topics = topics.split(",").filter { it.isNotBlank() },
        onboardingCompleted = onboardingCompleted
    )
}