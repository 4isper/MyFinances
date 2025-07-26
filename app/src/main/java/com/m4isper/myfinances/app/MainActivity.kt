package com.m4isper.myfinances.app

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.m4isper.settings.ThemeViewModel
import com.m4isper.ui.theme.MyFinancesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
//        val splashScreen = installSplashScreen()

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            installSplashScreen().setKeepOnScreenCondition { false }
//        }
        super.onCreate(savedInstanceState)
//        splashScreen.setKeepOnScreenCondition { false }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED

        enableEdgeToEdge()
        setContent {

            val factory = (application as MyFinancesApp).appComponent
                .activityComponent()
                .create()
                .provideThemeViewModelFactory()

            val viewModel: ThemeViewModel = viewModel(factory = factory)
            val isDarkTheme by viewModel.isDarkTheme.collectAsState()

            MyFinancesTheme(isDarkTheme) {
                AppRoot()
            }
        }
    }
}