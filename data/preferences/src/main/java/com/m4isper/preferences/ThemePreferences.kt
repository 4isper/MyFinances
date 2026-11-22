package com.m4isper.preferences

import android.content.Context
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeWriteMode
import eu.anifantakis.lib.ksafe.asWritableFlow
import kotlinx.coroutines.flow.Flow

class ThemePreferences(
    context: Context,
) {
    private val ksafe = KSafe(context)

    private val darkTheme by ksafe.asWritableFlow(
        false,
        key = "dark_theme_enabled",
        mode = KSafeWriteMode.Plain,
    )

    val isDarkTheme: Flow<Boolean> get() = darkTheme

    fun setDarkTheme(enabled: Boolean) {
        darkTheme.set(enabled)
    }
}
