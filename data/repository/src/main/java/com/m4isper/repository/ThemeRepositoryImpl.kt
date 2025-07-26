package com.m4isper.repository

import android.content.Context
import com.m4isper.domain.repository.ThemeRepository
import com.m4isper.preferences.ThemePreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ThemeRepositoryImpl @Inject constructor(
    private val context: Context
) : ThemeRepository {
    override fun isDarkTheme(): Flow<Boolean> = ThemePreferences.isDarkTheme(context)

    override suspend fun setDarkTheme(enabled: Boolean) {
        ThemePreferences.setDarkTheme(context, enabled)
    }
}
