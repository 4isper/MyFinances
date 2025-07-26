package com.m4isper.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ThemePreferences {
    private val Context.dataStore by preferencesDataStore(name = "settings")

    private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme_enabled")

    fun isDarkTheme(context: Context): Flow<Boolean> =
        context.dataStore.data.map { it[DARK_THEME_KEY] ?: false }

    suspend fun setDarkTheme(context: Context, enabled: Boolean) {
        context.dataStore.edit { it[DARK_THEME_KEY] = enabled }
    }
}
