package com.example.simpledictionary.domain.repository

import com.example.simpledictionary.domain.model.WordInfo
import com.example.simpledictionary.util.Resource
import kotlinx.coroutines.flow.Flow

interface WordInfoRepository {

    fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>>

    fun getSavedWordInfos(): Flow<List<WordInfo>>

    suspend fun deleteSavedWordInfo(word: String)

    suspend fun saveWordInfo(wordInfo: WordInfo)
}