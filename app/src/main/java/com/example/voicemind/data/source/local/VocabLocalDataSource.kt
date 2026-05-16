package com.example.voicemind.data.source.local

import com.example.voicemind.data.local.dao.VocabDao
import com.example.voicemind.data.local.entity.VocabSetEntity
import com.example.voicemind.data.local.entity.WordEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VocabLocalDataSource @Inject constructor(
    private val vocabDao: VocabDao
) {
    fun getSetsByUser(userId: String): Flow<List<VocabSetEntity>> {
        return vocabDao.getSetsByUser(userId)
    }

    fun getWordsBySet(setId: String): Flow<List<WordEntity>> {
        return vocabDao.getWordsBySet(setId)
    }

    fun getSetById(setId: String): Flow<VocabSetEntity> = vocabDao.getSetById(setId)

    suspend fun updateTotalWords(setId: String, totalWords: Int) {
        vocabDao.updateTotalWords(setId, totalWords)
    }

    suspend fun insertSet(set: VocabSetEntity) {
        vocabDao.insertSet(set)
    }

    suspend fun insertWords(words: List<WordEntity>) {
        vocabDao.insertWords(words)
    }

    suspend fun insertSetWithWords(set: VocabSetEntity, words: List<WordEntity>) {
        vocabDao.insertSetWithWords(set, words)
    }

    suspend fun insertSets(sets: List<VocabSetEntity>) {
        sets.forEach { vocabDao.insertSet(it) }
    }

    suspend fun deleteSet(setId: String) {
        vocabDao.deleteSet(setId)
    }
}
