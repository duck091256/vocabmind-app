package com.example.voicemind.domain.repository

import com.example.voicemind.domain.model.User

interface AuthRepository {
    val currentUser: User?
    suspend fun signInWithEmail(email: String, password: String): Result<User>
    suspend fun signUpWithEmail(email: String, password: String): Result<User>
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun signOut()
}