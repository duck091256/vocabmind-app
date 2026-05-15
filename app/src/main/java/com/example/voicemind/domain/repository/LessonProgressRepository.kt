package com.example.voicemind.domain.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LessonProgressRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) {
    private val userId: String? get() = authRepository.currentUser?.uid

    private fun getCompletedLessonsDoc() = userId?.let {
        firestore.collection("user_lesson_progress").document(it)
    }

    suspend fun markLessonCompleted(lessonId: String) {
        val doc = getCompletedLessonsDoc() ?: return
        val data = mapOf(
            "completedLessons" to com.google.firebase.firestore.FieldValue.arrayUnion(lessonId)
        )
        doc.set(data, com.google.firebase.firestore.SetOptions.merge()).await()
    }

    suspend fun getCompletedLessons(): Set<String> {
        val doc = getCompletedLessonsDoc() ?: return emptySet()
        val snapshot = doc.get().await()
        val list = snapshot.get("completedLessons") as? List<*> ?: return emptySet()
        return list.filterIsInstance<String>().toSet()
    }

    suspend fun isLessonCompleted(lessonId: String): Boolean {
        return lessonId in getCompletedLessons()
    }
}