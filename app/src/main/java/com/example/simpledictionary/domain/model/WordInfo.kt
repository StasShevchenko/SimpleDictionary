package com.example.simpledictionary.domain.model

import com.example.simpledictionary.data.local.db.entity.WordInfoEntity

data class WordInfo(
    val meanings: List<Meaning>,
    val phonetic: String,
    val word: String
) {
    fun toWordInfoEntity(): WordInfoEntity{
        return WordInfoEntity(
            meanings = meanings,
            phonetic = phonetic,
            word = word
        )
    }
}
