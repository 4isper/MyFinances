package com.m4isper.myfinances.di

import android.content.Context
import androidx.room.Room
import com.m4isper.myfinances.data.local.AppDatabase
import com.m4isper.myfinances.data.local.dao.TransactionDao
import com.m4isper.myfinances.domain.repository.AccountsRepository
import com.m4isper.myfinances.data.repository.AccountsRepositoryImpl
import com.m4isper.myfinances.domain.repository.CategoriesRepository
import com.m4isper.myfinances.data.repository.CategoriesRepositoryImpl
import com.m4isper.myfinances.data.repository.CurrencyRepositoryImpl
import com.m4isper.myfinances.domain.repository.TransactionRepository
import com.m4isper.myfinances.data.repository.TransactionRepositoryImpl
import com.m4isper.myfinances.domain.repository.CurrencyRepository
import com.m4isper.myfinances.domain.usecase.AddTransactionUseCase
import com.m4isper.myfinances.domain.usecase.DeleteTransactionUseCase
import com.m4isper.myfinances.domain.usecase.GetAccountByIdUseCase
import com.m4isper.myfinances.domain.usecase.GetAccountsUseCase
import com.m4isper.myfinances.domain.usecase.GetCategoriesUseCase
import com.m4isper.myfinances.domain.usecase.GetExpenseTransactionsUseCase
import com.m4isper.myfinances.domain.usecase.GetIncomeTransactionsUseCase
import com.m4isper.myfinances.domain.usecase.GetTransactionByIdUseCase
import com.m4isper.myfinances.domain.usecase.UpdateAccountUseCase
import com.m4isper.myfinances.domain.usecase.UpdateTransactionUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideDatabase(): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "myfinances_db").build()

    @Provides
    fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao()


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