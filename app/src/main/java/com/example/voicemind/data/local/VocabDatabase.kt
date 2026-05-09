package com.example.voicemind.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.voicemind.data.local.entity.UserProfileEntity
import com.example.voicemind.data.local.entity.VocabSetEntity
import com.example.voicemind.data.local.entity.WordEntity
import com.example.voicemind.data.local.dao.UserProfileDao
import com.example.voicemind.data.local.dao.VocabDao

@Database(
    entities = [
        UserProfileEntity::class,
        VocabSetEntity::class,
        WordEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class VocabDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun vocabDao(): VocabDao
}