package com.m4isper.domain.usecase

import com.m4isper.common.Result
import com.m4isper.domain.model.TransactionModel
import com.m4isper.domain.repository.TransactionRepository
import javax.inject.Inject

class UpdateTransactionUseCase
    @Inject
    constructor(
        private val repository: TransactionRepository,
    ) {
        suspend operator fun invoke(
            transactionId: Int,
            accountId: Int,
            categoryId: Int,
            amount: String,
            transactionDate: String,
            comment: String?,
        ): Result<TransactionModel> =
            repository.updateTransaction(
                transactionId,
                accountId,
                categoryId,
                amount,
                transactionDate,
                comment,
            )
    }
