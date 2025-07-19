package com.m4isper.myfinances.domain.usecase

import com.m4isper.myfinances.domain.repository.TransactionRepository
import com.m4isper.myfinances.domain.utils.Result
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transactionId: Int): Result<Unit> {
        return repository.deleteTransaction(transactionId)
    }
}