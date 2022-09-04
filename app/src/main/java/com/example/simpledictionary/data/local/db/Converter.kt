package com.example.simpledictionary.data.local.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.simpledictionary.data.util.JsonParser
import com.example.simpledictionary.domain.model.Meaning
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class Converter(
    private val jsonParser: JsonParser
) {

    @TypeConverter
    fun fromMeaningsJson(json: String): List<Meaning> {
        return jsonParser.fromJson<List<Meaning>>(
            json,
            object : TypeToken<List<Meaning>>(){}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toMeaningsJson(meanings: List<Meaning>): String {
        return jsonParser.toJson(
            meanings,
            object : TypeToken<List<Meaning>>(){}.type
        ) ?: "[]"
    }

}