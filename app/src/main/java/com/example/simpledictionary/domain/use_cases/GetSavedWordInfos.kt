package com.example.simpledictionary.domain.use_cases

import com.example.simpledictionary.domain.model.WordInfo
import com.example.simpledictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow

class GetSavedWordInfos(
    private val repository: WordInfoRepository
) {
    operator fun invoke(): Flow<List<WordInfo>> {
        return repository.getSavedWordInfos()
    }
}