package com.m4isper.myfinances.di

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
        NetworkModule::class
    ]
)
@Singleton
interface AppComponent {
    fun activityComponent(): ActivityComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            appModule: AppModule
        ): AppComponent
    }
}