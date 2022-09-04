package com.example.simpledictionary.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.simpledictionary.data.local.db.entity.WordInfoEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface WordInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWordInfoEntity(wordInfoEntity: WordInfoEntity)

    @Query("SELECT * FROM wordinfoentity ORDER BY word ASC")
    fun getSavedWordInfoEntities(): Flow<List<WordInfoEntity>>

    @Query("DELETE FROM wordinfoentity WHERE word = :word")
    fun deleteSavedWordInfoEntity(word: String)

}