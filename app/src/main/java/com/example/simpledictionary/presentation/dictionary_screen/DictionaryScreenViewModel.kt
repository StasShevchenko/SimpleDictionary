package com.example.simpledictionary.presentation.dictionary_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpledictionary.domain.model.WordInfo
import com.example.simpledictionary.domain.use_cases.GetSavedWordInfos
import com.example.simpledictionary.domain.use_cases.GetWordInfo
import com.example.simpledictionary.domain.use_cases.SaveWordInfo
import com.example.simpledictionary.util.Resource
import com.example.simpledictionary.util.ResourceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryScreenViewModel @Inject constructor(
    private val getWordInfo: GetWordInfo,
    private val getSavedWordInfos: GetSavedWordInfos,
    private val saveWordInfo: SaveWordInfo,
) : ViewModel() {

    private val _wordsState: MutableStateFlow<WordsUiState> = MutableStateFlow(WordsUiState())
    val wordsState: StateFlow<WordsUiState> = _wordsState

    private var savedWordsList: List<WordInfo> = emptyList()

    init {
        viewModelScope.launch {
            getSavedWordInfos().collectLatest { savedWords ->
                savedWordsList = savedWords
            }
        }
    }

    private var coroutineJob: Job? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow

    fun addWordToBookmarked(word: WordInfo){
        if (savedWordsList.contains(word)) {
            viewModelScope.launch {
                _eventFlow.emit(UiEvent.WordIsAlreadyExists)
            }
        }
        else {
            viewModelScope.launch {
                saveWordInfo(word)
                _eventFlow.emit(UiEvent.WordIsSaved)
            }
        }
    }

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

    sealed class UiEvent{
        object WordIsSaved: UiEvent()
        object WordIsAlreadyExists: UiEvent()
    }

}