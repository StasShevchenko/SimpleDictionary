package com.example.simpledictionary.domain.use_cases

import com.example.simpledictionary.domain.model.WordInfo
import com.example.simpledictionary.domain.repository.WordInfoRepository

class SaveWordInfo(
    private val repository: WordInfoRepository
) {
    suspend operator fun invoke(wordInfo: WordInfo) {
        repository.saveWordInfo(wordInfo)
    }
}