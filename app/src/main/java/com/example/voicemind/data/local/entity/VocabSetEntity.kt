package com.example.voicemind.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "vocab_sets")
data class VocabSetEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val totalWords: Int,
    val createdAt: Long,
    val userId: String
)