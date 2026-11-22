package com.m4isper.di

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.m4isper.domain.repository.AccountsRepository
import com.m4isper.domain.repository.CategoriesRepository
import com.m4isper.domain.repository.CurrencyRepository
import com.m4isper.domain.repository.TransactionRepository
import com.m4isper.local.AppDatabase
import com.m4isper.local.dao.AccountDao
import com.m4isper.local.dao.CategoryDao
import com.m4isper.local.dao.TransactionDao
import com.m4isper.preferences.CurrencyPreferences
import com.m4isper.repository.AccountsRepositoryImpl
import com.m4isper.repository.CategoriesRepositoryImpl
import com.m4isper.repository.CurrencyRepositoryImpl
import com.m4isper.repository.TransactionRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

// Use cases не требуют @Provides: у них @Inject-конструкторы, Dagger
// собирает их напрямую из графа. Модуль держит только привязки интерфейсов.
@Module
class AppModule(
    private val context: Context,
) {
    @Provides
    @Singleton
    fun provideDatabase(): AppDatabase =
        Room
            .databaseBuilder(context, AppDatabase::class.java, "myfinances_db")
            .setDriver(BundledSQLiteDriver())
            .build()

    @Provides
    fun provideAccountDao(db: AppDatabase): AccountDao = db.accountDao()

    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()

    @Provides
    fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao()

    @Provides
    @Singleton
    fun provideCurrencyPreferences(context: Context): CurrencyPreferences = CurrencyPreferences(context)

    @Provides
    @Singleton
    fun provideTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository = impl

    @Provides
    @Singleton
    fun provideCategoriesRepository(impl: CategoriesRepositoryImpl): CategoriesRepository = impl

    @Provides
    @Singleton
    fun provideAccountsRepository(impl: AccountsRepositoryImpl): AccountsRepository = impl

    @Provides
    @Singleton
    fun provideCurrencyRepository(impl: CurrencyRepositoryImpl): CurrencyRepository = impl
}
