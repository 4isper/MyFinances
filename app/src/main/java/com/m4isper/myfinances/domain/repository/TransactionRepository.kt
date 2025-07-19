package com.m4isper.myfinances.domain.repository

import com.m4isper.myfinances.domain.model.TransactionModel
import com.m4isper.myfinances.domain.model.TransactionResponseModel
import com.m4isper.myfinances.domain.utils.Result

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
    ): Result<TransactionResponseModel>

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


    suspend fun addTransactionLocal(transaction: TransactionModel)
    suspend fun getTransactionsLocal(): List<TransactionModel>
    suspend fun getUnsyncedTransactions(): List<TransactionModel>
    suspend fun markAsSynced(id: Int)
}