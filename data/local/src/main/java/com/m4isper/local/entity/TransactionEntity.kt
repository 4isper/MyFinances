package com.m4isper.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

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
