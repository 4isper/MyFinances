package com.m4isper.domain.usecase

import com.m4isper.common.Result
import com.m4isper.domain.model.TransactionModel
import com.m4isper.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionByIdUseCase
    @Inject
    constructor(
        private val transactionRepository: TransactionRepository,
    ) {
        suspend operator fun invoke(transactionId: Int): Result<TransactionModel> = transactionRepository.getTransactionById(transactionId)
    }
