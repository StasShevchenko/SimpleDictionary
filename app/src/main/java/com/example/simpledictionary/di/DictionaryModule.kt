package com.example.simpledictionary.di

import android.app.Application
import androidx.room.Room
import com.example.simpledictionary.data.local.db.Converter
import com.example.simpledictionary.data.local.db.DictionaryDatabase
import com.example.simpledictionary.data.remote.DictionaryApi
import com.example.simpledictionary.data.repository.WordInfoRepositoryImpl
import com.example.simpledictionary.data.util.GsonParser
import com.example.simpledictionary.domain.repository.WordInfoRepository
import com.example.simpledictionary.domain.use_cases.DeleteSavedWordInfo
import com.example.simpledictionary.domain.use_cases.GetSavedWordInfos
import com.example.simpledictionary.domain.use_cases.GetWordInfo
import com.example.simpledictionary.domain.use_cases.SaveWordInfo
import com.example.simpledictionary.util.ResourceManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DictionaryModule {

    @Provides
    fun provideDeleteSavedWordInfo(
        repository: WordInfoRepository
    ): DeleteSavedWordInfo {
        return DeleteSavedWordInfo(repository)
    }

    @Provides
    fun provideGetSavedWordInfos(
        repository: WordInfoRepository
    ): GetSavedWordInfos {
        return GetSavedWordInfos(repository)
    }

    @Provides
    fun provideSaveWordInfo(
        repository: WordInfoRepository
    ): SaveWordInfo {
        return SaveWordInfo(repository)
    }

    @Provides
    fun provideGetWordInfo(
        repository: WordInfoRepository
    ): GetWordInfo {
        return GetWordInfo(repository)
    }

    @Singleton
    @Provides
    fun provideWordInfoRepository(
        api: DictionaryApi,
        db: DictionaryDatabase,
        resourceManager: ResourceManager
    ): WordInfoRepository {
        return WordInfoRepositoryImpl(api, db.wordInfoDao, resourceManager)
    }

    @Singleton
    @Provides
    fun provideDictionaryApi(): DictionaryApi {
        return Retrofit.Builder()
            .baseUrl(DictionaryApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DictionaryApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDictionaryDatabase(app: Application): DictionaryDatabase {
        return Room.databaseBuilder(
            app,
            DictionaryDatabase::class.java, "dictionary_db"
        )
            .addTypeConverter(Converter(GsonParser(Gson())))
            .build()
    }

}