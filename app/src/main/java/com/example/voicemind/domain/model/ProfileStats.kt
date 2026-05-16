// domain/model/ProfileStats.kt
package com.example.voicemind.domain.model

// Sử dụng chung data class ForgettingStats đã có
data class ForgettingStats(
    val level1: Int = 0,
    val level2: Int = 0,
    val level3: Int = 0,
    val level4: Int = 0,
    val level5: Int = 0,
    val mastered: Int = 0
)