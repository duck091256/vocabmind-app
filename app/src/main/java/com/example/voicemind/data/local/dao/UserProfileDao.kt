package com.example.voicemind.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.voicemind.data.local.entity.UserProfileEntity

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: UserProfileEntity)

    @Query("SELECT * FROM user_profile WHERE uid = :uid LIMIT 1")
    suspend fun getByUid(uid: String?): UserProfileEntity?
}