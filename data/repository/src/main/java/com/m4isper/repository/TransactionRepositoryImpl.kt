package com.m4isper.repository

import com.m4isper.common.safeApiCall
import com.m4isper.domain.model.TransactionModel
import com.m4isper.domain.model.TransactionResponseModel
import com.m4isper.domain.repository.TransactionRepository
//import com.m4isper.local.dao.TransactionDao
import com.m4isper.common.Result
import com.m4isper.remote.SHMRFinanceApi
import com.m4isper.remote.TransactionRequestDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val api: SHMRFinanceApi,
//    private val transactionDao: TransactionDao
) : TransactionRepository {

    override suspend fun getTransactions(
        accountId: Int,
        startDate: String,
        endDate: String
    ): Result<List<TransactionModel>> = withContext(Dispatchers.IO) {
        val result = safeApiCall {
            api.getTransactions(accountId, startDate, endDate)
        }

        when (result) {
            is Result.Success -> {
                val data = result.data.map { it.toDomain()}
                Result.Success(data)
            }
            is Result.Failure -> result
        }
    }

    override suspend fun getTransactionById(
        transactionId: Int
    ): Result<TransactionModel> = withContext(Dispatchers.IO) {
        val result = safeApiCall {
            api.getTransactionById(transactionId)
        }

        when (result) {
            is Result.Success -> {
                val data = result.data.toDomain()
                Result.Success(data)
            }
            is Result.Failure -> result
        }
    }

    override suspend fun getIncomeTransactions(
        accountId: Int,
        startDate: String,
        endDate: String
    ): Result<List<TransactionModel>> = withContext(Dispatchers.IO) {
         when (val result = getTransactions(accountId, startDate, endDate)) {
            is Result.Success -> Result.Success(result.data
                .filter { it.isIncome })
            is Result.Failure -> result
        }
    }

    override suspend fun getExpenseTransactions(
        accountId: Int,
        startDate: String,
        endDate: String
    ): Result<List<TransactionModel>>  = withContext(Dispatchers.IO) {
         when (val result = getTransactions(accountId, startDate, endDate)) {
            is Result.Success -> Result.Success(result.data.filter { !it.isIncome })
            is Result.Failure -> result
        }
    }

    override suspend fun addTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): Result<TransactionResponseModel> = withContext(Dispatchers.IO) {
        val dto = TransactionRequestDto(
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = transactionDate,
            comment = comment
        )

        val result = safeApiCall {
            api.addTransaction(dto)
        }

        when (result){
            is Result.Success -> {
                val data = result.data.toDomain()
                Result.Success(data)
            }
            is Result.Failure -> result
        }
    }

    override suspend fun updateTransaction(
        transactionId: Int,
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): Result<TransactionModel> = withContext(Dispatchers.IO) {
        val dto = TransactionRequestDto(
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = transactionDate,
            comment = comment
        )

        val result = safeApiCall {
            api.updateTransaction(transactionId,dto)
        }

        when (result) {
            is Result.Success -> {
                val data = result.data.toDomain()
                Result.Success(data)
            }

            is Result.Failure -> result
        }
    }

    override suspend fun deleteTransaction(
        transactionId: Int
    ): Result<Unit> = withContext(Dispatchers.IO) {
        when (val result = safeApiCall { api.deleteTransaction(transactionId) }) {
            is Result.Success -> Result.Success(Unit)
            is Result.Failure -> result
        }
    }

//    override suspend fun addTransactionLocal(transaction: TransactionModel) {
//        transactionDao.insert(transaction.toEntity())
//    }
//
//    override suspend fun getTransactionsLocal(): List<TransactionModel> {
//        return transactionDao.getAll().map { it.toModel() }
//    }
//
//    override suspend fun getUnsyncedTransactions(): List<TransactionModel> {
//        return transactionDao.getUnsynced().map { it.toModel() }
//    }
//
//    override suspend fun markAsSynced(id: Int) {
//        val tx = transactionDao.getById(id) ?: return
//        transactionDao.insert(tx.copy(isSynced = true))
//    }
}
