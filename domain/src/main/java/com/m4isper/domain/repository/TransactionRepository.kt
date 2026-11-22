package com.m4isper.domain.repository

import com.m4isper.common.Result
import com.m4isper.domain.model.TransactionModel

interface TransactionRepository {
    suspend fun getTransactions(accountId: Int, startDate: String, endDate: String): Result<List<TransactionModel>>

    suspend fun getTransactionById(transactionId: Int): Result<TransactionModel>

    suspend fun getIncomeTransactions(
        accountId: Int, startDate: String, endDate: String
    ): Result<List<TransactionModel>>

    suspend fun getExpenseTransactions(
        accountId: Int, startDate: String, endDate: String
    ): Result<List<TransactionModel>>

    suspend fun addTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): Result<TransactionModel>

    suspend fun updateTransaction(
        transactionId: Int,
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): Result<TransactionModel>

    suspend fun deleteTransaction(
        transactionId: Int
    ): Result<Unit>
}
