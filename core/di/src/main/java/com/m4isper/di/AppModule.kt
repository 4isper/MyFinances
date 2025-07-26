package com.m4isper.di

import android.content.Context
import com.m4isper.domain.repository.AccountsRepository
import com.m4isper.domain.repository.CategoriesRepository
import com.m4isper.domain.repository.CurrencyRepository
import com.m4isper.domain.repository.TransactionRepository
import com.m4isper.domain.usecase.AddTransactionUseCase
import com.m4isper.domain.usecase.DeleteTransactionUseCase
import com.m4isper.domain.usecase.GetAccountByIdUseCase
import com.m4isper.domain.usecase.GetAccountsUseCase
import com.m4isper.domain.usecase.GetCategoriesUseCase
import com.m4isper.domain.usecase.GetExpenseTransactionsUseCase
import com.m4isper.domain.usecase.GetIncomeTransactionsUseCase
import com.m4isper.domain.usecase.GetTransactionByIdUseCase
import com.m4isper.domain.usecase.UpdateAccountUseCase
import com.m4isper.domain.usecase.UpdateTransactionUseCase
import com.m4isper.repository.AccountsRepositoryImpl
import com.m4isper.repository.CategoriesRepositoryImpl
import com.m4isper.repository.CurrencyRepositoryImpl
import com.m4isper.repository.TransactionRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

//    @Provides
//    @Singleton
//    fun provideDatabase(): AppDatabase =
//        Room.databaseBuilder(context, AppDatabase::class.java, "myfinances_db").build()
//
//    @Provides
//    fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao()


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

    @Provides
    fun provideGetTransactionByIdUseCase(
        repository: TransactionRepository
    ): GetTransactionByIdUseCase = GetTransactionByIdUseCase(repository)

    @Provides
    fun provideAddTransactionUseCase(
        repository: TransactionRepository
    ): AddTransactionUseCase = AddTransactionUseCase(repository)

    @Provides
    fun provideUpdateTransactionUseCase(
        repository: TransactionRepository
    ): UpdateTransactionUseCase = UpdateTransactionUseCase(repository)

    @Provides
    fun provideDeleteTransactionUseCase(
        repository: TransactionRepository
    ): DeleteTransactionUseCase = DeleteTransactionUseCase(repository)
}