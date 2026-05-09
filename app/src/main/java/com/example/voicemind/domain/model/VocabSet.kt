package com.example.voicemind.domain.model

data class VocabSet(
    val id: String,
    val title: String,
    val description: String,
    val totalWords: Int,
    val createdAt: Long,
    val userId: String,
    val words: List<Word> = emptyList()
)