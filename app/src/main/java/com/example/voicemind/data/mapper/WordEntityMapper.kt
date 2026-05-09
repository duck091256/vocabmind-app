package com.example.voicemind.data.mapper

import com.example.voicemind.data.local.entity.WordEntity
import com.example.voicemind.domain.model.Word

fun WordEntity.toDomain() = Word(
    id = id,
    setId = setId,
    word = word,
    meaning = meaning,
    example = example
)

fun Word.toEntity() = WordEntity(
    id = id,
    setId = setId,
    word = word,
    meaning = meaning,
    example = example
)
