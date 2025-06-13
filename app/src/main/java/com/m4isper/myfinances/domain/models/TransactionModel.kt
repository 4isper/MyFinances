package com.m4isper.myfinances.domain.models

data class TransactionModel(
    val id: Int,
    val account: AccountModel,
    val category: CategoryModel,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
)
