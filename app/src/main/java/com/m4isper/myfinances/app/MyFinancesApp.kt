package com.m4isper.myfinances.app

import android.app.Application
import com.m4isper.myfinances.di.AppComponent
import com.m4isper.myfinances.di.AppModule
import com.m4isper.myfinances.di.DaggerAppComponent

class MyFinancesApp : Application() {
    lateinit var appComponent: AppComponent
//        private set

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create(
            this,
            AppModule(this)
        )
    }
}
