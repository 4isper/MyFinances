package com.m4isper.domain.usecase

import com.m4isper.common.Result
import com.m4isper.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transactionId: Int): Result<Unit> {
        return repository.deleteTransaction(transactionId)
    }
}