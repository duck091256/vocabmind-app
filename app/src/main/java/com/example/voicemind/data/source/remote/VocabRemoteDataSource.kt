package com.example.voicemind.data.source.remote

import com.example.voicemind.domain.model.VocabSet
import com.example.voicemind.domain.model.Word
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VocabRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun getSetsByUser(userId: String): Flow<List<VocabSet>> = callbackFlow {
        val listener = firestore.collection("users")
            .document(userId)
            .collection("sets")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val sets = snapshot.documents.mapNotNull { doc ->
                        try {
                            VocabSet(
                                id = doc.getString("id") ?: doc.id,
                                title = doc.getString("title") ?: "",
                                description = doc.getString("description") ?: "",
                                totalWords = doc.getLong("totalWords")?.toInt() ?: 0,
                                createdAt = doc.getLong("createdAt") ?: 0L,
                                userId = doc.getString("userId") ?: userId
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                    trySend(sets)
                }
            }
        awaitClose { listener.remove() }
    }

    suspend fun createSet(set: VocabSet, words: List<Word>) {
        val batch = firestore.batch()
        
        val setRef = firestore.collection("users")
            .document(set.userId)
            .collection("sets")
            .document(set.id)
            
        batch.set(setRef, mapOf(
            "id" to set.id,
            "title" to set.title,
            "description" to set.description,
            "totalWords" to set.totalWords,
            "createdAt" to set.createdAt,
            "userId" to set.userId
        ))
        
        words.forEach { word ->
            val wordRef = setRef.collection("words").document(word.id)
            batch.set(wordRef, mapOf(
                "id" to word.id,
                "setId" to word.setId,
                "word" to word.word,
                "meaning" to word.meaning,
                "example" to word.example
            ))
        }
        
        batch.commit().await()
    }

    suspend fun deleteSet(userId: String, setId: String) {
        firestore.collection("users")
            .document(userId)
            .collection("sets")
            .document(setId)
            .delete()
            .await()
    }
}
