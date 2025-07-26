package com.m4isper.domain.usecase.pref

import com.m4isper.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDarkThemeUseCase @Inject constructor(
    private val repository: ThemeRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.isDarkTheme()
}