package com.m4isper.myfinances.data.model

data class TransactionDto(
    val id: Int,
    val account: AccountDto,
    val category: CategoryDto,
    val amount: String,
    val transactionDate: String,
    val comment: String,
    val createdAt: String,
    val updatedAt: String,
)

data class AccountDto(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String,
)

data class CategoryDto(
    val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean,
)

data class TransactionRequestDto(
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
)
