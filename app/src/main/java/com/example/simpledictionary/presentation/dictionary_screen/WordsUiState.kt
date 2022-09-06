package com.example.simpledictionary.presentation.dictionary_screen

import com.example.simpledictionary.domain.model.WordInfo

data class WordsUiState(
    val words: List<WordInfo> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null
)
