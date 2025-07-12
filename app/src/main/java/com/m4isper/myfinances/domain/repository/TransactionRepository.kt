package com.m4isper.myfinances.domain.repository

import com.m4isper.myfinances.domain.model.TransactionModel
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
}