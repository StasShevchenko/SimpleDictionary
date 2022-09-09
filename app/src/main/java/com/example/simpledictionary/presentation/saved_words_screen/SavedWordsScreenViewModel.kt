package com.example.simpledictionary.presentation.saved_words_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpledictionary.domain.model.WordInfo
import com.example.simpledictionary.domain.use_cases.DeleteSavedWordInfo
import com.example.simpledictionary.domain.use_cases.GetSavedWordInfos
import com.example.simpledictionary.domain.use_cases.SaveWordInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedWordsScreenViewModel @Inject constructor(
    private val getSavedWordInfos: GetSavedWordInfos,
    private val deleteSavedWordInfo: DeleteSavedWordInfo,
    private val saveWordInfo: SaveWordInfo
) : ViewModel() {

    private var currentExpandedPosition = -1

    private val _savedWords: MutableStateFlow<List<SavedWordInfo>> =
        MutableStateFlow(emptyList())
    val savedWords: StateFlow<List<SavedWordInfo>> = _savedWords

    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow

    init {
        viewModelScope.launch {
            getSavedWordInfos().collectLatest { savedWords ->
                val expandableList = savedWords.map {
                    SavedWordInfo.ExpandableWordInfo(
                        isExpanded = savedWords.indexOf(it) == currentExpandedPosition,
                        word = it
                    )
                }
                _savedWords.value = toSavedWordsList(expandableList)
            }
        }
    }

    fun saveDeletedWord(wordInfo: WordInfo) {
        viewModelScope.launch {
            saveWordInfo(wordInfo)
        }
    }

    fun deleteSavedWord(wordInfo: WordInfo) {
        viewModelScope.launch {
            deleteSavedWordInfo(wordInfo.word)
            currentExpandedPosition = -1
            _eventFlow.emit(UiEvent.WordIsDeleted(wordInfo))
        }
    }

    fun changeExpandedPosition(expandableWordInfoPosition: Int) {
        if (expandableWordInfoPosition == currentExpandedPosition) {
            val newList: MutableList<SavedWordInfo> = _savedWords.value.toMutableList()
            newList[expandableWordInfoPosition] =
                (_savedWords.value[expandableWordInfoPosition] as SavedWordInfo.ExpandableWordInfo).copy(
                    isExpanded = false
                )
            _savedWords.value = newList
            currentExpandedPosition = -1
        } else {
            val newList: MutableList<SavedWordInfo> = _savedWords.value.toMutableList()
            if (currentExpandedPosition != -1) {
                newList[currentExpandedPosition] =
                    (_savedWords.value[currentExpandedPosition] as SavedWordInfo.ExpandableWordInfo).copy(
                        isExpanded = false
                    )
            }
            currentExpandedPosition = expandableWordInfoPosition
            newList[expandableWordInfoPosition] =
                (_savedWords.value[expandableWordInfoPosition] as SavedWordInfo.ExpandableWordInfo).copy(
                    isExpanded = true
                )
            _savedWords.value = newList
        }
    }

    private fun toSavedWordsList(expandableList: List<SavedWordInfo.ExpandableWordInfo>): List<SavedWordInfo> {
        if (expandableList.isEmpty()) return emptyList()
        val resultList: MutableList<SavedWordInfo> = mutableListOf()
        var currentLetter = expandableList[0].word.word[0]
        resultList.add(SavedWordInfo.WordInfoHeader(currentLetter.toString()))
        for (i in expandableList.indices) {
            if (expandableList[i].word.word[0] == currentLetter) {
                resultList.add(expandableList[i])
            } else {
                currentLetter = expandableList[i].word.word[0]
                resultList.add(SavedWordInfo.WordInfoHeader(currentLetter.toString()))
                resultList.add(expandableList[i])
            }
        }
        return resultList
    }

    sealed class UiEvent {
        data class WordIsDeleted(val wordInfo: WordInfo) : UiEvent()
    }
}