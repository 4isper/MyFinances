package com.m4isper.myfinances.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.m4isper.myfinances.data.local.dao.TransactionDao
import com.m4isper.myfinances.data.local.entity.TransactionEntity

@Database(entities = [TransactionEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}
