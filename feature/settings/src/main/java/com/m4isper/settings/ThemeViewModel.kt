package com.m4isper.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4isper.domain.usecase.pref.GetDarkThemeUseCase
import com.m4isper.domain.usecase.pref.SetDarkThemeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ThemeViewModel (
    private val getDarkTheme: GetDarkThemeUseCase,
    private val setDarkTheme: SetDarkThemeUseCase
) : ViewModel() {
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    init {
        viewModelScope.launch {
            getDarkTheme().collect {
                _isDarkTheme.value = it
            }
        }
    }

    fun toggleTheme(enabled: Boolean) {
        viewModelScope.launch {
            setDarkTheme(enabled)
        }
    }
}
