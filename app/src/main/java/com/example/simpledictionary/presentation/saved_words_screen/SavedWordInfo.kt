package com.example.simpledictionary.presentation.saved_words_screen

import com.example.simpledictionary.domain.model.WordInfo

sealed class SavedWordInfo{
    data class ExpandableWordInfo(
        val isExpanded: Boolean = false,
        val word: WordInfo
    ) : SavedWordInfo()
    data class WordInfoHeader(
        val letter: String
    ) : SavedWordInfo()
}
