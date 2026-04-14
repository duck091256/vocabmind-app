package com.example.voicemind.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.voicemind.data.local.entity.UserProfileEntity
import com.example.voicemind.data.local.dao.UserProfileDao


@Database(
    entities = [UserProfileEntity::class],
    version = 1,
    exportSchema = false
)

abstract class VocabDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
}