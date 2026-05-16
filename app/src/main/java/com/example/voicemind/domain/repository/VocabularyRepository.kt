package com.example.voicemind.domain.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class VocabularyWord(
    val word: String,
    val definition: String,
    val example: String,
    val level: String,
    val topic: String,
    val pronunciation: String = ""
)

class VocabularyRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getAllWords(): List<VocabularyWord> {
        return try {
            val snapshot = firestore.collection("vocabulary").get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.data?.let {
                    VocabularyWord(
                        word = it["word"] as? String ?: "",
                        definition = it["definition"] as? String ?: "",
                        example = it["example"] as? String ?: "",
                        level = it["level"] as? String ?: "",
                        topic = it["topic"] as? String ?: "",
                        pronunciation = it["pronunciation"] as? String ?: ""
                    )
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getWordSet(): Set<String> {
        return getAllWords().map { it.word.lowercase() }.toSet()
    }

    suspend fun getWordsByLevel(level: String): List<VocabularyWord> {
        return getAllWords().filter { it.level == level }
    }

    suspend fun getWordsByTopic(topic: String): List<VocabularyWord> {
        return getAllWords().filter { it.topic == topic }
    }
}