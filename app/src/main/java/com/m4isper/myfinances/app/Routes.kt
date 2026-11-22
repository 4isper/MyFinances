package com.m4isper.myfinances.app

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation3.runtime.NavKey
import com.m4isper.resources.R
import kotlinx.serialization.Serializable

// Nav3-маршруты — типобезопасные ключи. Сериализация нужна rememberNavBackStack
// для персистентности бэкстеков (замена saveState/restoreState из Nav2).
@Serializable
data object Expenses : NavKey

@Serializable
data object Income : NavKey

@Serializable
data object Account : NavKey

@Serializable
data object Categories : NavKey

@Serializable
data object Settings : NavKey

@Serializable
data object ExpensesHistory : NavKey

@Serializable
data object IncomeHistory : NavKey

@Serializable
data object ExpensesAnalysis : NavKey

@Serializable
data object IncomeAnalysis : NavKey

@Serializable
data object AccountEdit : NavKey

@Serializable
data object SettingsAppInfo : NavKey

@Serializable
data class TransactionKey(
    val id: Int,
    // id = -1 — «новая транзакция»: тип приходит из контекста экрана-источника
    val isIncome: Boolean,
) : NavKey

/**
 * Верхнеуровневые табы нижней навигации: ключ + ресурсы для отображения.
 * Порядок списка = порядок элементов в navigation bar.
 */
data class TopLevelDestination(
    val key: NavKey,
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int,
)

val topLevelDestinations =
    listOf(
        TopLevelDestination(Expenses, R.string.expenses, R.drawable.ic_expenses),
        TopLevelDestination(Income, R.string.incomes, R.drawable.ic_income),
        TopLevelDestination(Account, R.string.account, R.drawable.ic_account),
        TopLevelDestination(Categories, R.string.categories, R.drawable.ic_categories),
        TopLevelDestination(Settings, R.string.settings, R.drawable.ic_settings),
    )
