package com.m4isper.myfinances.di

import com.m4isper.myfinances.app.MainActivity
import com.m4isper.myfinances.ui.screens.accountScreen.AccountViewModelFactory
import com.m4isper.myfinances.ui.screens.categoriesScreen.CategoriesViewModelFactory
import com.m4isper.myfinances.ui.screens.expensesScreen.ExpensesViewModelFactory
import com.m4isper.myfinances.ui.screens.historyScreen.HistoryViewModelFactory
import com.m4isper.myfinances.ui.screens.incomeScreen.IncomeViewModelFactory
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface ActivityComponent {
    fun inject(activity: MainActivity)

    fun provideAccountViewModelFactory(): AccountViewModelFactory
    fun provideIncomeViewModelFactory(): IncomeViewModelFactory
    fun provideExpensesViewModelFactory(): ExpensesViewModelFactory
    fun provideHistoryViewModelFactory(): HistoryViewModelFactory
    fun provideCategoriesViewModelFactory(): CategoriesViewModelFactory
    fun transactionComponent(): TransactionComponent.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }
}