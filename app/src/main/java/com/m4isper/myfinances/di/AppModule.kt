package com.m4isper.myfinances.di

import com.m4isper.myfinances.domain.repository.AccountsRepository
import com.m4isper.myfinances.data.repository.AccountsRepositoryImpl
import com.m4isper.myfinances.domain.repository.CategoriesRepository
import com.m4isper.myfinances.data.repository.CategoriesRepositoryImpl
import com.m4isper.myfinances.data.repository.CurrencyRepositoryImpl
import com.m4isper.myfinances.domain.repository.TransactionRepository
import com.m4isper.myfinances.data.repository.TransactionRepositoryImpl
import com.m4isper.myfinances.domain.repository.CurrencyRepository
import com.m4isper.myfinances.domain.usecase.GetAccountByIdUseCase
import com.m4isper.myfinances.domain.usecase.GetAccountsUseCase
import com.m4isper.myfinances.domain.usecase.GetCategoriesUseCase
import com.m4isper.myfinances.domain.usecase.GetExpenseTransactionsUseCase
import com.m4isper.myfinances.domain.usecase.GetIncomeTransactionsUseCase
import com.m4isper.myfinances.domain.usecase.UpdateAccountUseCase
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
    @Singleton
    fun provideCurrencyRepository(
        impl: CurrencyRepositoryImpl
    ): CurrencyRepository = impl


    @Provides
    fun provideGetAccountsUseCase(
        repository: AccountsRepository
    ): GetAccountsUseCase = GetAccountsUseCase(repository)

    @Provides
    fun provideGetAccountByIdUseCase(
        repository: AccountsRepository
    ): GetAccountByIdUseCase = GetAccountByIdUseCase(repository)

    @Provides
    fun provideUpdateAccountUseCase(
        repository: AccountsRepository
    ): UpdateAccountUseCase = UpdateAccountUseCase(repository)

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