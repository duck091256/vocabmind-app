package com.example.voicemind.domain.repository

import com.example.voicemind.domain.model.WordProgress
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

class WordProgressRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) {
    private val userId: String? get() = authRepository.currentUser?.uid

    private fun getCollection() = userId?.let {
        firestore.collection("user_word_progress").document(it).collection("words")
    }

    suspend fun saveProgress(word: String, level: Int, nextReviewDate: Timestamp) {
        val collection = getCollection() ?: return
        val doc = collection.document(word.lowercase())
        val data = mapOf(
            "userId" to userId,
            "word" to word.lowercase(),
            "level" to level,
            "nextReviewDate" to nextReviewDate,
            "lastReviewed" to Timestamp.now()
        )
        doc.set(data).await()
    }

    suspend fun getWordsNeedReview(): List<WordProgress> {
        val collection = getCollection() ?: return emptyList()
        val now = Timestamp.now()
        val snapshot = collection
            .whereLessThanOrEqualTo("nextReviewDate", now)
            // Removed the exclusion for level 5/6, so all levels can be reviewed when due
            .get()
            .await()
        return snapshot.documents.mapNotNull { doc ->
            val level = doc.getLong("level")?.toInt() ?: return@mapNotNull null
            WordProgress(
                userId = userId!!,
                word = doc.getString("word") ?: return@mapNotNull null,
                level = level,
                nextReviewDate = doc.getTimestamp("nextReviewDate") ?: return@mapNotNull null,
                lastReviewed = doc.getTimestamp("lastReviewed")
            )
        }
    }

    suspend fun getDefinition(word: String): String? {
        return try {
            val snapshot = firestore.collection("vocabulary")
                .whereEqualTo("word", word.lowercase())
                .get()
                .await()
            snapshot.documents.firstOrNull()?.getString("definition")
        } catch (e: Exception) { null }
    }

    suspend fun updateReview(word: String, correct: Boolean) {
        val collection = getCollection() ?: return
        val docRef = collection.document(word.lowercase())
        val doc = docRef.get().await()
        val currentLevel = doc.getLong("level")?.toInt() ?: 1
        var newLevel = if (correct) currentLevel + 1 else 1
        if (newLevel > 6) newLevel = 6

        val delayHours = when (newLevel) {
            1 -> 2
            2 -> 24
            3 -> 48
            4 -> 72
            5 -> 120
            6 -> 192
            else -> 0
        }
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR_OF_DAY, delayHours)
        val nextReviewDate = Timestamp(cal.time)
        
        docRef.update(mapOf(
            "level" to newLevel,
            "nextReviewDate" to nextReviewDate,
            "lastReviewed" to Timestamp.now()
        )).await()
    }

    suspend fun getStats(): Map<Int, Int> {
        val collection = getCollection() ?: return emptyMap()
        val snapshot = collection.get().await()
        val counts = mutableMapOf<Int, Int>()
        for (i in 1..6) counts[i] = 0
        snapshot.documents.forEach { doc ->
            val level = doc.getLong("level")?.toInt() ?: return@forEach
            counts[level] = counts.getOrDefault(level, 0) + 1
        }
        return counts
    }

    suspend fun getStatsForWords(words: List<String>): Map<Int, Int> {
        val collection = getCollection() ?: return emptyMap()
        val snapshot = collection.get().await()
        val counts = mutableMapOf<Int, Int>()
        for (i in 1..6) counts[i] = 0
        
        val wordSet = words.map { it.lowercase() }.toSet()
        snapshot.documents.forEach { doc ->
            val word = doc.getString("word")?.lowercase() ?: return@forEach
            if (word in wordSet) {
                val level = doc.getLong("level")?.toInt() ?: return@forEach
                counts[level] = counts.getOrDefault(level, 0) + 1
            }
        }
        return counts
    }

    suspend fun getWordLevels(words: List<String>): Map<String, Int> {
        val collection = getCollection() ?: return emptyMap()
        val snapshot = collection.get().await()
        val levels = mutableMapOf<String, Int>()
        
        val wordSet = words.map { it.lowercase() }.toSet()
        snapshot.documents.forEach { doc ->
            val word = doc.getString("word")?.lowercase() ?: return@forEach
            if (word in wordSet) {
                val level = doc.getLong("level")?.toInt() ?: return@forEach
                levels[word] = level
            }
        }
        return levels
    }

    suspend fun getWordProgressMap(words: List<String>): Map<String, WordProgress> {
        val collection = getCollection() ?: return emptyMap()
        val snapshot = collection.get().await()
        val progressMap = mutableMapOf<String, WordProgress>()
        
        val wordSet = words.map { it.lowercase() }.toSet()
        snapshot.documents.forEach { doc ->
            val word = doc.getString("word")?.lowercase() ?: return@forEach
            if (word in wordSet) {
                val level = doc.getLong("level")?.toInt() ?: return@forEach
                val nextReviewDate = doc.getTimestamp("nextReviewDate") ?: return@forEach
                progressMap[word] = WordProgress(
                    userId = userId!!,
                    word = word,
                    level = level,
                    nextReviewDate = nextReviewDate,
                    lastReviewed = doc.getTimestamp("lastReviewed")
                )
            }
        }
        return progressMap
    }
}