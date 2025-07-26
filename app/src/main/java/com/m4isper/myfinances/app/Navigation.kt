package com.m4isper.myfinances.app

import androidx.annotation.StringRes
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.m4isper.account.AccountScreen
import com.m4isper.account.EditAccountScreen
import com.m4isper.analysis.AnalysisScreen
import com.m4isper.categories.CategoriesScreen
import com.m4isper.expenses.ExpensesScreen
import com.m4isper.history.HistoryScreen
import com.m4isper.income.IncomeScreen
import com.m4isper.resources.R
import com.m4isper.settings.navigation.SETTINGS_GRAPH_ROUTE
import com.m4isper.settings.navigation.settingsNavGraph
import com.m4isper.splash.LottieSplashScreen
import com.m4isper.transaction.TransactionScreen
import kotlin.collections.forEach
import kotlin.collections.forEachIndexed

enum class Destination(
    val route: String,
    @StringRes val label: Int,
    val iconId: Int,
    val contentDescription: String,
) {
    EXPENSES("expenses", R.string.expenses, R.drawable.ic_expenses, "Expenses"),
    INCOME("income", R.string.incomes, R.drawable.ic_income, "Income"),
    ACCOUNT("account", R.string.account, R.drawable.ic_account, "Account"),
    CATEGORIES("categories", R.string.categories, R.drawable.ic_categories, "Categories"),
    SETTINGS(SETTINGS_GRAPH_ROUTE, R.string.settings, R.drawable.ic_settings, "Settings"),
}

//private fun resolveCurrentTopLevelDestination(currentRoute: String?): Destination? {
//    return Destination.entries.firstOrNull { destination ->
//        currentRoute?.startsWith(destination.route) == true
//    }
//}

@Composable
private fun resolveCurrentTopLevelDestination(navController: NavHostController): Destination? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination ?: return null

    return Destination.entries.firstOrNull { topLevel ->
        destination.hierarchy.any { it.route == topLevel.route }
    }
}


@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activityComponent = remember {
        (context.applicationContext as MyFinancesApp)
            .appComponent
            .activityComponent()
            .create()
    }

    NavHost(
        navController = navController,
        startDestination = startDestination.route
    ) {
        Destination.entries.forEach { destination ->
            composable(destination.route) {
                when(destination) {
                    Destination.EXPENSES -> {
                        val factory = activityComponent.provideExpensesViewModelFactory()
                        ExpensesScreen(modifier, navController, factory)
                    }
                    Destination.INCOME -> {
                        val factory = activityComponent.provideIncomeViewModelFactory()
                        IncomeScreen(modifier, navController,  factory)
                    }
                    Destination.ACCOUNT -> {
                        val factory = activityComponent.provideAccountViewModelFactory()
                        AccountScreen(modifier, navController,  factory)
                    }
                    Destination.CATEGORIES -> {
                        val factory = activityComponent.provideCategoriesViewModelFactory()
                        CategoriesScreen(modifier,  factory)
                    }
                    Destination.SETTINGS -> {
//                        val factory = activityComponent.provideThemeViewModelFactory()
//                        settingsNavGraph(modifier, navController,factory)
                    }
                }
            }
        }

        settingsNavGraph(
            modifier = modifier,
            navController = navController,
            themeViewModelFactory = activityComponent.provideThemeViewModelFactory()
        )


        composable("income/history") {
            val factory = activityComponent.provideHistoryViewModelFactory()
            HistoryScreen(modifier, navController, type = "income", viewModelFactory = factory)
        }

        composable("expenses/history") {
            val factory = activityComponent.provideHistoryViewModelFactory()
            HistoryScreen(modifier, navController, type = "expenses", viewModelFactory = factory)
        }

        composable("income/analysis") {
            val factory = activityComponent.provideAnalysisViewModelFactory()
            AnalysisScreen(modifier, navController, type = "income", viewModelFactory = factory)
        }

        composable("expenses/analysis") {
            val factory = activityComponent.provideAnalysisViewModelFactory()
            AnalysisScreen(modifier, navController, type = "expenses", viewModelFactory = factory)
        }

        composable("account/edit") {
            val factory = activityComponent.provideAccountViewModelFactory()
            EditAccountScreen(modifier, navController, factory)
        }

        composable("transaction/{id}") { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            transactionId?.let {
                val factory = activityComponent
                    .transactionComponent()
                    .create(it)
                    .transactionViewModelFactory()

                TransactionScreen(modifier, navController, factory)
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
    val startDestination = Destination.ACCOUNT

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentTopLevelDestination = resolveCurrentTopLevelDestination(navController)

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
            ) {
                Destination.entries.forEachIndexed { index, destination ->
                    val isSelected = destination == currentTopLevelDestination
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (!isSelected) {
                                navController.navigate(destination.route) {
                                    popUpTo(startDestination.route) {
                                        inclusive = true
                                        saveState = true
                                    }
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
                            Text(stringResource(destination.label))
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