package com.m4isper.domain.usecase

import com.m4isper.common.Result
import com.m4isper.domain.model.TransactionResponseModel
import com.m4isper.domain.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): Result<TransactionResponseModel>{
        return repository.addTransaction(
            accountId,
            categoryId,
            amount,
            transactionDate,
            comment
        )
    }
}