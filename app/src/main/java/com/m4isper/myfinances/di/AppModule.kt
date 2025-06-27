package com.m4isper.myfinances.di

import com.m4isper.myfinances.data.repository.AccountsRepository
import com.m4isper.myfinances.data.repository.AccountsRepositoryImpl
import com.m4isper.myfinances.data.repository.CategoriesRepository
import com.m4isper.myfinances.data.repository.CategoriesRepositoryImpl
import com.m4isper.myfinances.data.repository.TransactionRepository
import com.m4isper.myfinances.data.repository.TransactionRepositoryImpl
import com.m4isper.myfinances.domain.usecase.GetAccountsUseCase
import com.m4isper.myfinances.domain.usecase.GetCategoriesUseCase
import com.m4isper.myfinances.domain.usecase.GetExpenseTransactionsUseCase
import com.m4isper.myfinances.domain.usecase.GetIncomeTransactionsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTransactionRepository(
        impl: TransactionRepositoryImpl
    ): TransactionRepository = impl

    @Provides
    @Singleton
    fun provideCategoriesRepository(
        impl: CategoriesRepositoryImpl
    ): CategoriesRepository = impl

    @Provides
    @Singleton
    fun provideAccountsRepository(
        impl: AccountsRepositoryImpl
    ): AccountsRepository = impl


    @Provides
    fun provideGetAccountsUseCase(
        repository: AccountsRepository
    ): GetAccountsUseCase = GetAccountsUseCase(repository)

    @Provides
    fun provideGetCategoriesUseCase(
        repository: CategoriesRepository
    ): GetCategoriesUseCase = GetCategoriesUseCase(repository)

    @Provides
    fun provideGetIncomeTransactionsUseCase(
        repository: TransactionRepository
    ): GetIncomeTransactionsUseCase = GetIncomeTransactionsUseCase(repository)

    @Provides
    fun provideGetExpenseTransactionsUseCase(
        repository: TransactionRepository
    ): GetExpenseTransactionsUseCase = GetExpenseTransactionsUseCase(repository)
}