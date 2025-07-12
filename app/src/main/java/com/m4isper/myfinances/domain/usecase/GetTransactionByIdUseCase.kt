package com.m4isper.myfinances.domain.usecase

import com.m4isper.myfinances.domain.model.TransactionModel
import com.m4isper.myfinances.domain.repository.TransactionRepository
import com.m4isper.myfinances.domain.utils.Result
import javax.inject.Inject

class GetTransactionByIdUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transactionId: Int): Result<TransactionModel> {
        return transactionRepository.getTransactionById(transactionId)
    }
}