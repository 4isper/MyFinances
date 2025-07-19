package com.m4isper.myfinances.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: Int,
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val comment: String?,
    val transactionDate: String,
    val isIncome: Boolean,
    val categoryName: String,
    val emoji: String,
    val currency: String,
    val isSynced: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)
