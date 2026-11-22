package com.m4isper.settings

import com.m4isper.domain.usecase.pref.GetDarkThemeUseCase
import com.m4isper.domain.usecase.pref.SetDarkThemeUseCase
import com.m4isper.testing.FakeThemeRepository
import com.m4isper.testing.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ThemeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun viewModelCollectsInitialThemeFromRepository() =
        runTest {
            val repository = FakeThemeRepository(initial = true)
            val viewModel = ThemeViewModel(GetDarkThemeUseCase(repository), SetDarkThemeUseCase(repository))

            // collect в init выполняется eagerly на подменённом Main-диспетчере
            assertEquals(true, viewModel.isDarkTheme.value)
        }

    @Test
    fun toggleThemePersistsToRepository() =
        runTest {
            val repository = FakeThemeRepository(initial = false)
            val viewModel = ThemeViewModel(GetDarkThemeUseCase(repository), SetDarkThemeUseCase(repository))

            viewModel.toggleTheme(true)

            assertEquals(listOf(true), repository.setDarkThemeCalls)
            assertEquals(true, viewModel.isDarkTheme.value)
        }
}
