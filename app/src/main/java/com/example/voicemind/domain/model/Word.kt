package com.example.voicemind.domain.model

data class Word(
    val id: String,
    val setId: String,
    val word: String,
    val meaning: String,
    val example: String?
)