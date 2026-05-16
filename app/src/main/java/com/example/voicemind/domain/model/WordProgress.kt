package com.example.voicemind.domain.model

import com.google.firebase.Timestamp

data class WordProgress(
    val userId: String = "",
    val word: String = "",
    var level: Int = 1,
    var nextReviewDate: Timestamp = Timestamp.now(),
    var lastReviewed: Timestamp? = null
) {
    // No-arg constructor for Firestore (bắt buộc)
    constructor() : this("", "", 1, Timestamp.now(), null)
}