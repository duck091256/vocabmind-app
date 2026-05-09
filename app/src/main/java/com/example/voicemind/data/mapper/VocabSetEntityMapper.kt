package com.example.voicemind.data.mapper

import com.example.voicemind.data.local.entity.VocabSetEntity
import com.example.voicemind.domain.model.VocabSet

fun VocabSetEntity.toDomain() = VocabSet(
    id = id,
    title = title,
    description = description,
    totalWords = totalWords,
    createdAt = createdAt,
    userId = userId
)

fun VocabSet.toEntity() = VocabSetEntity(
    id = id,
    title = title,
    description = description,
    totalWords = totalWords,
    createdAt = createdAt,
    userId = userId
)
