package com.example.simpledictionary.presentation.dictionary_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpledictionary.domain.model.WordInfo
import com.example.simpledictionary.domain.use_cases.GetWordInfo
import com.example.simpledictionary.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryScreenViewModel @Inject constructor(
    private val getWordInfo: GetWordInfo
) : ViewModel() {

    private val _wordsState: MutableStateFlow<WordsUiState> = MutableStateFlow(WordsUiState())
    val wordsState: StateFlow<WordsUiState> = _wordsState

    private var coroutineJob: Job? = null

    fun searchWord(word: String) {
        coroutineJob?.cancel()
        coroutineJob = viewModelScope.launch {
            delay(500L)
            if (word.isBlank()) {
                _wordsState.value = wordsState.value.copy(
                    words = emptyList(),
                    isLoading = false,
                    isError = false
                )
            } else {
                getWordInfo(word).collectLatest { wordsList ->
                    when (wordsList) {
                        is Resource.Success -> {
                            _wordsState.value = wordsState.value.copy(
                                words = wordsList.data!!,
                                isLoading = false,
                                isError = false
                            )
                        }
                        is Resource.Error -> {
                            _wordsState.value = wordsState.value.copy(
                                isError = true,
                                errorMessage = wordsList.message,
                                isLoading = false,
                                words = emptyList()
                            )
                        }
                        is Resource.Loading -> {
                            _wordsState.value = wordsState.value.copy(
                                isLoading = true,
                                isError = false,
                                words = emptyList()
                            )
                        }
                    }
                }
            }
        }
    }

}