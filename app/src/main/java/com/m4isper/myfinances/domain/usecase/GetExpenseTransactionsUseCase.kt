package com.m4isper.myfinances.domain.usecase

import com.m4isper.myfinances.domain.repository.TransactionRepository
import com.m4isper.myfinances.domain.utils.Result
import com.m4isper.myfinances.domain.model.TransactionModel
import javax.inject.Inject

class GetExpenseTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(
        accountId: Int,
        startDate: String,
        endDate: String
    ): Result<List<TransactionModel>> {
        return repository.getExpenseTransactions(accountId, startDate, endDate)
    }
}