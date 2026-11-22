package com.m4isper.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.m4isper.local.dao.AccountDao
import com.m4isper.local.dao.CategoryDao
import com.m4isper.local.dao.TransactionDao
import com.m4isper.local.entity.AccountEntity
import com.m4isper.local.entity.CategoryEntity
import com.m4isper.local.entity.TransactionEntity

@Database(
    entities = [AccountEntity::class, CategoryEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}
