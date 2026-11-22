package com.m4isper.myfinances.app

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.m4isper.account.AccountScreen
import com.m4isper.account.EditAccountScreen
import com.m4isper.analysis.AnalysisScreen
import com.m4isper.categories.CategoriesScreen
import com.m4isper.expenses.ExpensesScreen
import com.m4isper.history.HistoryScreen
import com.m4isper.income.IncomeScreen
import com.m4isper.resources.R
import com.m4isper.settings.SettingsScreen
import com.m4isper.settings.appInfoScreen.AppInfoScreen
import com.m4isper.splash.LottieSplashScreen
import com.m4isper.transaction.TransactionScreen

@Composable
fun AppRoot() {
    var showSplash by rememberSaveable { mutableStateOf(true) }

    Crossfade(targetState = showSplash) { splashVisible ->
        if (splashVisible) {
            LottieSplashScreen(onFinished = { showSplash = false })
        } else {
            AppNavigation()
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activityComponent =
        remember {
            (context.applicationContext as MyFinancesApp)
                .appComponent
                .activityComponent()
                .create()
        }

    val navigationState =
        rememberNavigationState(
            startRoute = Account,
            topLevelRoutes = topLevelDestinations.map { it.key }.toSet(),
        )
    val navigator = remember(navigationState) { Navigator(navigationState) }

    // without the override the two panes get a horizontal gap; see b/418201867
    val windowAdaptiveInfo = currentWindowAdaptiveInfoV2()
    val paneScaffoldDirective =
        remember(windowAdaptiveInfo) {
            calculatePaneScaffoldDirective(windowAdaptiveInfo)
                .copy(horizontalPartitionSpacerSize = 0.dp)
        }
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(directive = paneScaffoldDirective)

    val entryProvider =
        entryProvider<NavKey> {
            entry<Expenses>(
                metadata =
                    ListDetailSceneStrategy.listPane(
                        detailPlaceholder = {
                            DetailPlaceholder()
                        },
                    ),
            ) {
                val factory = activityComponent.provideExpensesViewModelFactory()
                ExpensesScreen(
                    modifier = modifier,
                    onHistory = { navigator.navigate(ExpensesHistory) },
                    onTransactionClick = { id -> navigator.navigate(TransactionKey(id)) },
                    viewModelFactory = factory,
                )
            }

            entry<Income>(
                metadata =
                    ListDetailSceneStrategy.listPane(
                        detailPlaceholder = {
                            DetailPlaceholder()
                        },
                    ),
            ) {
                val factory = activityComponent.provideIncomeViewModelFactory()
                IncomeScreen(
                    modifier = modifier,
                    onHistory = { navigator.navigate(IncomeHistory) },
                    onTransactionClick = { id -> navigator.navigate(TransactionKey(id)) },
                    viewModelFactory = factory,
                )
            }

            entry<Account> {
                val factory = activityComponent.provideAccountViewModelFactory()
                AccountScreen(
                    modifier = modifier,
                    onEditAccount = { navigator.navigate(AccountEdit) },
                    viewModelFactory = factory,
                )
            }

            entry<Categories> {
                val factory = activityComponent.provideCategoriesViewModelFactory()
                CategoriesScreen(modifier = modifier, viewModelFactory = factory)
            }

            entry<Settings> {
                val factory = activityComponent.provideThemeViewModelFactory()
                SettingsScreen(
                    modifier = modifier,
                    onAppInfo = { navigator.navigate(SettingsAppInfo) },
                    viewModelFactory = factory,
                )
            }

            entry<ExpensesHistory>(
                metadata =
                    ListDetailSceneStrategy.listPane(
                        detailPlaceholder = {
                            DetailPlaceholder()
                        },
                    ),
            ) {
                val factory = activityComponent.provideHistoryViewModelFactory()
                HistoryScreen(
                    modifier = modifier,
                    type = "expenses",
                    onBack = { navigator.goBack() },
                    onAnalysis = { navigator.navigate(ExpensesAnalysis) },
                    onTransactionClick = { id -> navigator.navigate(TransactionKey(id)) },
                    viewModelFactory = factory,
                )
            }

            entry<IncomeHistory>(
                metadata =
                    ListDetailSceneStrategy.listPane(
                        detailPlaceholder = {
                            DetailPlaceholder()
                        },
                    ),
            ) {
                val factory = activityComponent.provideHistoryViewModelFactory()
                HistoryScreen(
                    modifier = modifier,
                    type = "income",
                    onBack = { navigator.goBack() },
                    onAnalysis = { navigator.navigate(IncomeAnalysis) },
                    onTransactionClick = { id -> navigator.navigate(TransactionKey(id)) },
                    viewModelFactory = factory,
                )
            }

            entry<ExpensesAnalysis> {
                val factory = activityComponent.provideAnalysisViewModelFactory()
                AnalysisScreen(
                    modifier = modifier,
                    type = "expenses",
                    onBack = { navigator.goBack() },
                    onTransactionClick = { id -> navigator.navigate(TransactionKey(id)) },
                    viewModelFactory = factory,
                )
            }

            entry<IncomeAnalysis> {
                val factory = activityComponent.provideAnalysisViewModelFactory()
                AnalysisScreen(
                    modifier = modifier,
                    type = "income",
                    onBack = { navigator.goBack() },
                    onTransactionClick = { id -> navigator.navigate(TransactionKey(id)) },
                    viewModelFactory = factory,
                )
            }

            entry<AccountEdit> {
                val factory = activityComponent.provideAccountViewModelFactory()
                EditAccountScreen(
                    modifier = modifier,
                    onBack = { navigator.goBack() },
                    viewModelFactory = factory,
                )
            }

            entry<SettingsAppInfo> {
                AppInfoScreen(modifier = modifier, onBack = { navigator.goBack() })
            }

            entry<TransactionKey>(
                metadata = ListDetailSceneStrategy.detailPane(),
            ) { key ->
                val factory =
                    activityComponent
                        .transactionComponent()
                        .create(key.id)
                        .transactionViewModelFactory()
                TransactionScreen(
                    modifier = modifier,
                    onBack = { navigator.goBack() },
                    viewModelFactory = factory,
                )
            }
        }

    NavigationSuiteScaffold(
        modifier = modifier,
        navigationSuiteItems = {
            topLevelDestinations.forEach { destination ->
                val isSelected = destination.key == navigationState.topLevelRoute
                item(
                    selected = isSelected,
                    onClick = { if (!isSelected) navigator.navigate(destination.key) },
                    icon = {
                        Icon(
                            ImageVector.vectorResource(destination.iconRes),
                            contentDescription = null,
                        )
                    },
                    label = { Text(stringResource(destination.labelRes)) },
                )
            }
        },
    ) {
        NavDisplay(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            entries = navigationState.toEntries(entryProvider),
            onBack = { navigator.goBack() },
            sceneStrategies = listOf(listDetailStrategy),
        )
    }
}

/** Заглушка detail-pane, пока транзакция не выбрана (широкие экраны). */
@Composable
private fun DetailPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainer),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.select_transaction),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
