package com.example.simpledictionary.data.repository

import com.example.simpledictionary.data.remote.DictionaryApi
import com.example.simpledictionary.data.remote.dto.WordInfoDto
import com.example.simpledictionary.domain.model.WordInfo
import com.example.simpledictionary.domain.repository.WordInfoRepository
import com.example.simpledictionary.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WordInfoRepositoryImpl(
    private val api: DictionaryApi
) : WordInfoRepository {
    override suspend fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> = flow {
        emit(Resource.Loading())
        try {
            val remoteWordInfos = api.getWordInfo(word)
            emit(Resource.Success(data = remoteWordInfos.map { it.toWordInfo() }))
        } catch (e: HttpException) {
            emit(Resource.Error(message = "Oops, something went wrong!"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Oops, something went wrong!"))
        }
    }
}