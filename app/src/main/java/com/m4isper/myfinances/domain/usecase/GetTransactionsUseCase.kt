package com.m4isper.myfinances.domain.usecase

import com.m4isper.myfinances.domain.repository.TransactionRepository
import com.m4isper.myfinances.domain.model.TransactionModel
import javax.inject.Inject
import com.m4isper.myfinances.domain.utils.Result


class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(accountId: Int, startDate: String, endDate: String): Result<List<TransactionModel>> {
        return repository.getTransactions(accountId, startDate, endDate)
    }
}
