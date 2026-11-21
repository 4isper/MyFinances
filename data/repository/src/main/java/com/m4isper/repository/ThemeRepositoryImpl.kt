package com.m4isper.repository

import android.content.Context
import com.m4isper.domain.repository.ThemeRepository
import com.m4isper.preferences.ThemePreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ThemeRepositoryImpl @Inject constructor(
    context: Context
) : ThemeRepository {
    private val themePreferences = ThemePreferences(context)

    override fun isDarkTheme(): Flow<Boolean> = themePreferences.isDarkTheme

    override suspend fun setDarkTheme(enabled: Boolean) {
        themePreferences.setDarkTheme(enabled)
    }
}
