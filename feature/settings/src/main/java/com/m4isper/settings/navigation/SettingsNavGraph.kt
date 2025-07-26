package com.m4isper.settings.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.NavController
import com.m4isper.settings.SettingsScreen
import com.m4isper.settings.ThemeViewModelFactory
import com.m4isper.settings.appInfoScreen.AppInfoScreen

const val SETTINGS_GRAPH_ROUTE = "settings_graph"
const val SETTINGS_ROOT = "settings"
const val SETTINGS_APP_INFO = "settings/app_info"
const val SETTINGS_LOCALE = "settings/locale"

fun NavGraphBuilder.settingsNavGraph(
    modifier: Modifier = Modifier,
    navController: NavController,
    themeViewModelFactory: ThemeViewModelFactory
) {
    navigation(
        startDestination = SETTINGS_ROOT,
        route = SETTINGS_GRAPH_ROUTE
    ) {
        composable(SETTINGS_ROOT) {
            SettingsScreen(
                modifier = modifier,
                navController = navController,
                viewModelFactory = themeViewModelFactory
            )
        }
        composable(SETTINGS_APP_INFO) {
            AppInfoScreen(
                modifier = modifier,
                navController = navController,
            )
        }
        composable(SETTINGS_LOCALE) {
//            LocaleSettingsScreen()
        }
    }
}
