package com.example.simpledictionary.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simpledictionary.domain.model.Meaning
import com.example.simpledictionary.domain.model.WordInfo

@Entity
data class WordInfoEntity (
    val meanings: List<Meaning>,
    val phonetic: String?,
    val word: String,
    @PrimaryKey
    val wordInfoId: Int? = null
    ){
    fun toWordInfo(): WordInfo {
        return WordInfo(
            meanings = meanings,
            phonetic = phonetic,
            word = word
        )
    }
}