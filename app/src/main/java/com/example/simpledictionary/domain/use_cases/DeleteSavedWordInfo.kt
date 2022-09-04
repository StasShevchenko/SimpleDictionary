package com.example.simpledictionary.domain.use_cases

import com.example.simpledictionary.domain.model.WordInfo
import com.example.simpledictionary.domain.repository.WordInfoRepository

class DeleteSavedWordInfo(
    private val repository: WordInfoRepository
) {
    suspend operator fun invoke(word: String) {
        repository.deleteSavedWordInfo(word)
    }
}