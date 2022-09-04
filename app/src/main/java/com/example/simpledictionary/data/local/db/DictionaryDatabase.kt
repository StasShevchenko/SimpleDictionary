package com.example.simpledictionary.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.simpledictionary.data.local.db.entity.WordInfoEntity

@Database(
    entities = [WordInfoEntity::class],
    version = 1
)
@TypeConverters(Converter::class)
abstract class DictionaryDatabase : RoomDatabase() {

   abstract val wordInfoDao: WordInfoDao

}