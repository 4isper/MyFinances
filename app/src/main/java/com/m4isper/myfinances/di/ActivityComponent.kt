package com.m4isper.myfinances.di

import com.m4isper.account.AccountViewModelFactory
import com.m4isper.analysis.AnalysisScreenViewModelFactory
import com.m4isper.categories.CategoriesViewModelFactory
import com.m4isper.di.TransactionComponent
import com.m4isper.expenses.ExpensesViewModelFactory
import com.m4isper.history.HistoryViewModelFactory
import com.m4isper.income.IncomeViewModelFactory
import com.m4isper.myfinances.app.MainActivity
import com.m4isper.settings.ThemeViewModelFactory
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface ActivityComponent {
    fun inject(activity: MainActivity)

    fun provideAccountViewModelFactory(): AccountViewModelFactory
    fun provideIncomeViewModelFactory(): IncomeViewModelFactory
    fun provideExpensesViewModelFactory(): ExpensesViewModelFactory
    fun provideHistoryViewModelFactory(): HistoryViewModelFactory
    fun provideAnalysisViewModelFactory(): AnalysisScreenViewModelFactory
    fun provideCategoriesViewModelFactory(): CategoriesViewModelFactory
    fun transactionComponent(): TransactionComponent.Factory

    fun provideThemeViewModelFactory(): ThemeViewModelFactory

    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }
}