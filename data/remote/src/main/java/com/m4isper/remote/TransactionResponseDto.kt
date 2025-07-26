package com.m4isper.remote

data class TransactionResponseDto(
    val id: Int,
    val accountId: Int,
    val categoryId: String,
    val amount: String,
    val transactionDate: String,
    val comment: String,
    val createdAt: String,
    val updatedAt: String
)