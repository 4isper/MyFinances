package com.m4isper.myfinances.domain.usecase

import com.m4isper.myfinances.data.repository.TransactionRepository
import com.m4isper.myfinances.domain.utils.Result
import com.m4isper.myfinances.domain.model.TransactionModel
import javax.inject.Inject

class GetIncomeTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(accountId: Int, startDate: String, endDate: String): Result<List<TransactionModel>> {
        return repository.getIncomeTransactions(accountId, startDate, endDate)
    }
}