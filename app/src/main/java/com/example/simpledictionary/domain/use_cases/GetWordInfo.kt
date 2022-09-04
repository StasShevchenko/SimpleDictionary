package com.example.simpledictionary.domain.use_cases

import com.example.simpledictionary.domain.model.WordInfo
import com.example.simpledictionary.domain.repository.WordInfoRepository
import com.example.simpledictionary.util.Resource
import kotlinx.coroutines.flow.Flow

class GetWordInfo(
    private val repository: WordInfoRepository
) {
    operator fun invoke(word: String): Flow<Resource<List<WordInfo>>> {
        return repository.getWordInfo(word)
    }
}