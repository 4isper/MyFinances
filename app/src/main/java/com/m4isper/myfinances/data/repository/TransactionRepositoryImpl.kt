package com.m4isper.myfinances.data.repository

import com.m4isper.myfinances.data.model.toDomain
import com.m4isper.myfinances.data.remote.SHMRFinanceApi
import com.m4isper.myfinances.data.utils.safeApiCall
import com.m4isper.myfinances.domain.model.TransactionModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.m4isper.myfinances.domain.utils.Result

class TransactionRepositoryImpl @Inject constructor(
    private val api: SHMRFinanceApi
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
}
