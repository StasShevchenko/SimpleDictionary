package com.example.simpledictionary.data.remote.dto

import com.example.simpledictionary.domain.model.Definition

data class DefinitionDto(
    val antonyms: List<String>,
    val definition: String,
    val example: String?,
    val synonyms: List<String>
){
    fun toDefinition(): Definition {
        return Definition(
            example = example,
            definition = definition
        )
    }
}