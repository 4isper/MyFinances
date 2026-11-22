package com.m4isper.domain.model

data class TransactionModel(
    val id: Int,
    val accountId: Int,
    val isIncome: Boolean,
    val categoryName: String,
    val categoryId: Int,
    val emoji: String,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val currency: String
)
