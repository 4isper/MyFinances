package com.m4isper.domain.usecase

import com.m4isper.common.Result
import com.m4isper.domain.model.TransactionModel
import com.m4isper.domain.repository.TransactionRepository
import javax.inject.Inject

class GetIncomeTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(accountId: Int, startDate: String, endDate: String): Result<List<TransactionModel>> {
        return repository.getIncomeTransactions(accountId, startDate, endDate)
    }
}