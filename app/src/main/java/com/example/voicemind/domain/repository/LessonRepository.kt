package com.example.voicemind.domain.repository

import android.util.Log
import com.example.voicemind.domain.model.Lesson
import com.example.voicemind.domain.model.WordItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LessonRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun getAllLessons(): List<Lesson> {
        return try {
            Log.d("LessonRepository", "Bắt đầu lấy dữ liệu từ Firestore...")
            val snapshot = firestore.collection("vocabulary").get().await()
            Log.d("LessonRepository", "Lấy thành công ${snapshot.documents.size} documents")

            val allWords = snapshot.documents.mapNotNull { doc ->
                val word = doc.getString("word") ?: return@mapNotNull null
                WordItem(
                    word = word,
                    definition = doc.getString("definition") ?: "",
                    example = doc.getString("example") ?: "",
                    pronunciation = doc.getString("pronunciation") ?: "",
                    level = doc.getString("level") ?: "",
                    topic = doc.getString("topic") ?: ""
                )
            }
            Log.d("LessonRepository", "Số từ hợp lệ: ${allWords.size}")

            if (allWords.isEmpty()) return emptyList()

            // Nhóm theo topic hoặc level
            val groupByTopic = allWords.any { it.topic.isNotBlank() }
            Log.d("LessonRepository", "Nhóm theo topic? $groupByTopic")

            val grouped = if (groupByTopic) {
                allWords.groupBy { it.topic }
            } else {
                allWords.groupBy { it.level.ifBlank { "Chung" } }
            }

            Log.d("LessonRepository", "Số nhóm: ${grouped.size}")

            val lessons = mutableListOf<Lesson>()
            for ((groupName, words) in grouped) {
                val chunked = words.chunked(10)
                for ((index, chunk) in chunked.withIndex()) {
                    val lessonId = "${groupName.replace(" ", "_")}_${index + 1}"
                    val title = if (chunked.size == 1) "📘 $groupName" else "📘 $groupName (phần ${index + 1})"
                    lessons.add(
                        Lesson(
                            id = lessonId,
                            title = title,
                            description = "${chunk.size} từ vựng",
                            words = chunk
                        )
                    )
                }
            }
            Log.d("LessonRepository", "Tạo thành công ${lessons.size} bài học")
            lessons
        } catch (e: Exception) {
            Log.e("LessonRepository", "Lỗi khi lấy dữ liệu: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun getLessonById(lessonId: String): Lesson? {
        return getAllLessons().find { it.id == lessonId }
    }
}