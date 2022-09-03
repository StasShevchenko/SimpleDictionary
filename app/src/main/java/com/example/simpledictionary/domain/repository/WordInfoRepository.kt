package com.example.simpledictionary.domain.repository

import com.example.simpledictionary.domain.model.WordInfo
import com.example.simpledictionary.util.Resource
import kotlinx.coroutines.flow.Flow

interface WordInfoRepository {

    suspend fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>>

}