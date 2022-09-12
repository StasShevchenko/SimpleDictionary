package com.example.simpledictionary.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

enum class AppTheme{
    VIOLET, BLUE, GREEN
}

data class AppPreferences(val theme: AppTheme)

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore = context.dataStore

    val preferencesFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val appTheme = AppTheme.valueOf(preferences[PreferencesKeys.APP_THEME] ?: AppTheme.VIOLET.name)
            AppPreferences(appTheme)
        }


    suspend fun updateAppTheme(appTheme: AppTheme) {
        dataStore.edit { prefereces ->
            prefereces[PreferencesKeys.APP_THEME] = appTheme.name
        }

    }

    private object PreferencesKeys {
        val APP_THEME = stringPreferencesKey("app_theme")
    }

}