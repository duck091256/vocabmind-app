package com.example.voicemind.domain.usecase

import com.example.voicemind.domain.repository.VocabRepository
import javax.inject.Inject

class GetAllSetsUseCase @Inject constructor(private val repo: VocabRepository) {
    operator fun invoke() = repo.getAllSets()
}

class GetRecentSetsUseCase @Inject constructor(private val repo: VocabRepository) {
    operator fun invoke() = repo.getRecentSets()
}

class GetWordsBySetUseCase @Inject constructor(private val repo: VocabRepository) {
    operator fun invoke(setId: String) = repo.getWordsBySet(setId)
}

class GetSetByIdUseCase @Inject constructor(private val repo: VocabRepository) {
    operator fun invoke(setId: String) = repo.getSetById(setId)
}

class CreateSetUseCase @Inject constructor(private val repo: VocabRepository) {
    suspend operator fun invoke(
        title: String,
        description: String,
        words: List<Pair<String, String>>,
        userId: String
    ) = repo.createSet(title, description, words, userId)
}