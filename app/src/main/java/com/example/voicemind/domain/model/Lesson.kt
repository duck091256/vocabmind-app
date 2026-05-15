package com.example.voicemind.domain.model

data class Lesson(
    val id: String,
    val title: String,
    val description: String,
    val words: List<WordItem>
)

data class WordItem(
    val word: String,
    val definition: String,
    val example: String,
    val pronunciation: String = "",
    val level: String = "",
    val topic: String = ""
)