package com.m4isper.myfinances.di

import android.content.Context
import com.m4isper.di.AppModule
import com.m4isper.di.NetworkModule
import com.m4isper.di.PreferencesModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        PreferencesModule::class
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