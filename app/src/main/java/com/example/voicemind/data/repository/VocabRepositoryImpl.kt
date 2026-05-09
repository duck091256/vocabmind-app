package com.example.voicemind.data.repository

import com.example.voicemind.data.mapper.toDomain
import com.example.voicemind.data.mapper.toEntity
import com.example.voicemind.data.local.entity.WordEntity
import com.example.voicemind.data.source.local.VocabLocalDataSource
import com.example.voicemind.data.source.remote.VocabRemoteDataSource
import com.example.voicemind.domain.model.Resource
import com.example.voicemind.domain.model.VocabSet
import com.example.voicemind.domain.model.Word
import com.example.voicemind.domain.repository.AuthRepository
import com.example.voicemind.domain.repository.VocabRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class VocabRepositoryImpl(
    private val localDataSource: VocabLocalDataSource,
    private val remoteDataSource: VocabRemoteDataSource,
    private val authRepository: AuthRepository
) : VocabRepository {

    override fun getAllSets(): Flow<Resource<List<VocabSet>>> = flow {
        emit(Resource.Loading)
        val userId = authRepository.currentUser?.uid ?: run {
            emit(Resource.Error("User not logged in"))
            return@flow
        }
        
        // Return local flow first for offline support, sync will happen in background
        emitAll(
            localDataSource.getSetsByUser(userId)
                .map { entities -> Resource.Success(entities.map { it.toDomain() }) }
                .catch { e -> emit(Resource.Error(e.message ?: "Unknown error", Exception(e))) }
        )
    }

    override fun getSetsByUser(userId: String): Flow<Resource<List<VocabSet>>> = flow {
        emit(Resource.Loading)
        emitAll(
            localDataSource.getSetsByUser(userId)
                .map { entities -> Resource.Success(entities.map { it.toDomain() }) }
                .catch { e -> emit(Resource.Error(e.message ?: "Unknown error", Exception(e))) }
        )
    }

    override fun getRecentSets(): Flow<Resource<List<VocabSet>>> = flow {
        emit(Resource.Loading)
        // Not fully implemented yet, just returning empty for now
        emit(Resource.Success(emptyList()))
    }

    override fun getWordsBySet(setId: String): Flow<Resource<List<Word>>> = flow {
        emit(Resource.Loading)
        // Not fully implemented yet, returning empty
        emit(Resource.Success(emptyList()))
    }

    override suspend fun createSet(
        title: String,
        description: String,
        words: List<Pair<String, String>>,
        userId: String
    ): Resource<Unit> {
        return try {
            val setId = UUID.randomUUID().toString()
            val totalWords = words.size
            val set = VocabSet(
                id = setId,
                title = title,
                description = description,
                totalWords = totalWords,
                createdAt = System.currentTimeMillis(),
                userId = userId
            )
            
            val wordModels = words.map { (term, definition) ->
                Word(
                    id = UUID.randomUUID().toString(),
                    setId = setId,
                    word = term,
                    meaning = definition,
                    example = ""
                )
            }
            
            // 1. Save to Remote
            remoteDataSource.createSet(set, wordModels)
            // 2. Save to Local
            val wordEntities = wordModels.map { 
                WordEntity(id = it.id, setId = it.setId, word = it.word, meaning = it.meaning, example = it.example)
            }
            localDataSource.insertSetWithWords(set.toEntity(), wordEntities)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to create set", e)
        }
    }

    override suspend fun deleteSet(setId: String): Resource<Unit> {
        return try {
            val userId = authRepository.currentUser?.uid ?: return Resource.Error("Not logged in")
            // 1. Delete from Remote
            remoteDataSource.deleteSet(userId, setId)
            // 2. Delete from Local
            localDataSource.deleteSet(setId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to delete set", e)
        }
    }

    override suspend fun syncSetsForUser(userId: String): Resource<Unit> {
        return try {
            val remoteSets = remoteDataSource.getSetsByUser(userId).first()
            localDataSource.insertSets(remoteSets.map { it.toEntity() })
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Sync failed", e)
        }
    }
}