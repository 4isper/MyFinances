package com.m4isper.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m4isper.domain.usecase.pref.GetDarkThemeUseCase
import com.m4isper.domain.usecase.pref.SetDarkThemeUseCase
import javax.inject.Inject

class ThemeViewModelFactory @Inject constructor(
    private val getDarkThemeUseCase: GetDarkThemeUseCase,
    private val setDarkThemeUseCase: SetDarkThemeUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemeViewModel::class.java))
            return ThemeViewModel(
                getDarkThemeUseCase,
                setDarkThemeUseCase
            )
                    as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
