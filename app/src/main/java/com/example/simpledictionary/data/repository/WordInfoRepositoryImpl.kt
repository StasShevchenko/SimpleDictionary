package com.example.simpledictionary.data.repository

import com.example.simpledictionary.R
import com.example.simpledictionary.data.local.db.WordInfoDao
import com.example.simpledictionary.data.remote.DictionaryApi
import com.example.simpledictionary.domain.model.WordInfo
import com.example.simpledictionary.domain.repository.WordInfoRepository
import com.example.simpledictionary.util.Resource
import com.example.simpledictionary.util.ResourceManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class WordInfoRepositoryImpl(
    private val api: DictionaryApi,
    private val dao: WordInfoDao,
    private val resourceManager: ResourceManager
) : WordInfoRepository {

    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> = flow {
        emit(Resource.Loading())
        try {
            val remoteWordInfos = api.getWordInfo(word)
            emit(Resource.Success(data = remoteWordInfos.map { it.toWordInfo() }))
        } catch (e: HttpException) {
            emit(Resource.Error(message = resourceManager.getString(R.string.internet_error_message)))
        } catch (e: IOException) {
            emit(Resource.Error(message = resourceManager.getString(R.string.internet_error_message)))
        }
    }

    override fun getSavedWordInfos(): Flow<List<WordInfo>> {
        return dao.getSavedWordInfoEntities().map { entitiesList ->
            entitiesList.map { wordInfoEntity ->
                wordInfoEntity.toWordInfo()
            }
        }
    }

    override suspend fun deleteSavedWordInfo(word: String) {
        dao.deleteSavedWordInfoEntity(word)
    }

    override suspend fun saveWordInfo(wordInfo: WordInfo) {
        dao.saveWordInfoEntity(
            wordInfo.toWordInfoEntity()
        )
    }
}