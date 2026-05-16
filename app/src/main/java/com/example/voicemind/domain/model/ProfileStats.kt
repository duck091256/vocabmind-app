// domain/model/ProfileStats.kt
package com.example.voicemind.domain.model

// Sử dụng chung data class ForgettingStats đã có
data class ForgettingStats(
    val after1Hour: Int = 0,
    val after1Day: Int = 0,
    val after3Days: Int = 0,
    val after1Week: Int = 0,
    val permanent: Int = 0
)