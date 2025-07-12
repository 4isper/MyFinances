package com.m4isper.myfinances.app

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.m4isper.myfinances.ui.theme.MyFinancesTheme

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
            MyFinancesTheme {
                AppRoot()
            }
        }
    }
}