package com.m4isper.myfinances

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.m4isper.myfinances.ui.screens.AccountScreen
import com.m4isper.myfinances.ui.screens.CategoriesScreen
import com.m4isper.myfinances.ui.screens.ExpensesScreen
import com.m4isper.myfinances.ui.screens.IncomeScreen
import com.m4isper.myfinances.ui.screens.LottieSplashScreen
import com.m4isper.myfinances.ui.screens.SettingsScreen
import kotlin.collections.forEach
import kotlin.collections.forEachIndexed

enum class Destination(
    val route: String,
    val label: String,
    val iconId: Int,
    val contentDescription: String
) {
    EXPENSES("expenses", "Расходы", R.drawable.ic_expenses, "Expenses"),
    INCOME("income", "Доходы", R.drawable.ic_income, "Income"),
    ACCOUNT("account", "Счет", R.drawable.ic_account, "Account"),
    CATEGORIES("categories", "Статьи", R.drawable.ic_categories, "Categories"),
    SETTINGS("settings", "Настройки", R.drawable.ic_settings, "Settings"),
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route
    ) {
        Destination.entries.forEach { destination ->
            composable(destination.route) {
                when(destination) {
                    Destination.EXPENSES -> ExpensesScreen(modifier)
                    Destination.INCOME -> IncomeScreen(modifier)
                    Destination.ACCOUNT -> AccountScreen(modifier)
                    Destination.CATEGORIES -> CategoriesScreen(modifier)
                    Destination.SETTINGS -> SettingsScreen(modifier)
                }
            }
        }
    }
}

@Composable
fun AppRoot() {
    var showSplash by rememberSaveable { mutableStateOf(true) }

    Crossfade(targetState = showSplash) { splashVisible ->
        if (splashVisible) {
            LottieSplashScreen(onFinished = { showSplash = false })
        } else {
            MainNavigationBar()
        }
    }
}


@Composable
fun MainNavigationBar(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = Destination.EXPENSES
//    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar(
                windowInsets = WindowInsets(
                    left = 16,
                    right = 16,
                    bottom = NavigationBarDefaults.windowInsets.getBottom(LocalDensity.current)
                ),
                containerColor = MaterialTheme.colorScheme.surfaceContainer
//                windowInsets = NavigationBarDefaults.windowInsets
            ) {
                Destination.entries.forEachIndexed { index, destination ->
                    val isSelected = destination.route == currentRoute
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (destination.route != currentRoute) {
                                navController.navigate(destination.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                ImageVector.vectorResource(destination.iconId),
                                contentDescription = destination.contentDescription
                            )
                        },
                        label = {
                            Text(destination.label)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.secondary
                        )
                    )
                }
            }
        }
    ) { contentPadding ->
        AppNavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .padding(contentPadding)
                .background(MaterialTheme.colorScheme.surface)
        )
    }
}