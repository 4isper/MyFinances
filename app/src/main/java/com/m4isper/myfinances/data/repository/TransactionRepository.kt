package com.m4isper.myfinances.data.repository

import com.m4isper.myfinances.domain.utils.Result
import com.m4isper.myfinances.domain.model.TransactionModel

interface TransactionRepository {
    suspend fun getTransactions(accountId: Int, startDate: String, endDate: String): Result<List<TransactionModel>>
    suspend fun getIncomeTransactions(
        accountId: Int, startDate: String, endDate: String
    ): Result<List<TransactionModel>>
    suspend fun getExpenseTransactions(
        accountId: Int, startDate: String, endDate: String
    ): Result<List<TransactionModel>>
}