package com.m4isper.domain.usecase

import com.m4isper.common.Result
import com.m4isper.domain.model.TransactionModel
import com.m4isper.domain.repository.TransactionRepository
import com.m4isper.domain.utils.InvalidAmountException
import com.m4isper.domain.utils.toCleanDecimal
import java.math.BigDecimal
import javax.inject.Inject

/** Сумма должна быть положительным числом; мусорные строки отсекаются парсером. */
internal fun invalidAmountOrNull(amount: String): InvalidAmountException? =
    if (amount.toCleanDecimal() <= BigDecimal.ZERO) InvalidAmountException(amount) else null

class AddTransactionUseCase
    @Inject
    constructor(
        private val repository: TransactionRepository,
    ) {
        suspend operator fun invoke(
            accountId: Int,
            categoryId: Int,
            amount: String,
            transactionDate: String,
            comment: String?,
        ): Result<TransactionModel> {
            invalidAmountOrNull(amount)?.let { return Result.Failure(it) }
            return repository.addTransaction(
                accountId,
                categoryId,
                amount,
                transactionDate,
                comment,
            )
        }
    }
