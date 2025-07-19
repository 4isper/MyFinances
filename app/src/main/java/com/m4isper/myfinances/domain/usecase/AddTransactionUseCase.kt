package com.m4isper.myfinances.domain.usecase

import com.m4isper.myfinances.domain.model.TransactionResponseModel
import com.m4isper.myfinances.domain.repository.TransactionRepository
import com.m4isper.myfinances.domain.utils.Result
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