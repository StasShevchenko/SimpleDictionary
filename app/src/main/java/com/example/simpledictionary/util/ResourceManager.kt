package com.example.simpledictionary.util

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceManager @Inject constructor(@ApplicationContext private val context: Context) {
    fun getString(@StringRes id: Int): String {
        return context.getString(id)
    }
}