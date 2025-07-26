package com.m4isper.domain.usecase.pref

import com.m4isper.domain.repository.ThemeRepository
import javax.inject.Inject


class SetDarkThemeUseCase @Inject constructor(
    private val repository: ThemeRepository
) {
    suspend operator fun invoke(enabled: Boolean) = repository.setDarkTheme(enabled)
}