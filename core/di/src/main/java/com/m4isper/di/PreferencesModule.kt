package com.m4isper.di

import android.content.Context
import com.m4isper.domain.repository.ThemeRepository
import com.m4isper.domain.usecase.pref.GetDarkThemeUseCase
import com.m4isper.domain.usecase.pref.SetDarkThemeUseCase
import com.m4isper.repository.ThemeRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object PreferencesModule {

    @Provides
    @Singleton
    fun provideThemeRepository(context: Context): ThemeRepository =
        ThemeRepositoryImpl(context)

    @Provides
    fun provideGetDarkThemeUseCase(repository: ThemeRepository): GetDarkThemeUseCase =
        GetDarkThemeUseCase(repository)

    @Provides
    fun provideSetDarkThemeUseCase(repository: ThemeRepository): SetDarkThemeUseCase =
        SetDarkThemeUseCase(repository)
}