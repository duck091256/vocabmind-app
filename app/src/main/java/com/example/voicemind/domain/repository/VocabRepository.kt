package com.example.voicemind.domain.repository

import com.example.voicemind.domain.model.Resource
import com.example.voicemind.domain.model.VocabSet
import com.example.voicemind.domain.model.Word
import kotlinx.coroutines.flow.Flow

interface VocabRepository {
    fun getAllSets(): Flow<Resource<List<VocabSet>>>
    fun getSetsByUser(userId: String): Flow<Resource<List<VocabSet>>>
    fun getRecentSets(): Flow<Resource<List<VocabSet>>>
    fun getWordsBySet(setId: String): Flow<Resource<List<Word>>>

    suspend fun createSet(
        title: String,
        description: String,
        words: List<Pair<String, String>>,
        userId: String
    ): Resource<Unit>

    suspend fun deleteSet(setId: String): Resource<Unit>
    
    suspend fun syncSetsForUser(userId: String): Resource<Unit>
}