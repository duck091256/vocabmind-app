package com.example.voicemind.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import com.example.voicemind.data.local.entity.VocabSetEntity
import com.example.voicemind.data.local.entity.WordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VocabDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(set: VocabSetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<WordEntity>)

    @Query("SELECT * FROM vocab_sets ORDER BY createdAt DESC")
    fun getAllSets(): Flow<List<VocabSetEntity>>

    @Query("SELECT * FROM vocab_sets WHERE userId = :userId ORDER BY createdAt DESC")
    fun getSetsByUser(userId: String): Flow<List<VocabSetEntity>>

    @Query("SELECT * FROM vocab_sets ORDER BY createdAt DESC LIMIT 5")
    fun getRecentSets(): Flow<List<VocabSetEntity>>

    @Query("SELECT * FROM words WHERE setId = :setId")
    fun getWordsBySet(setId: String): Flow<List<WordEntity>>

    @Query("DELETE FROM vocab_sets WHERE id = :setId")
    suspend fun deleteSet(setId: String)

    @Transaction
    suspend fun insertSetWithWords(
        set: VocabSetEntity,
        words: List<WordEntity>
    ) {
        insertSet(set)
        insertWords(words)
    }
}