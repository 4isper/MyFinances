package com.m4isper.domain.utils

import com.m4isper.domain.model.TransactionModel
import java.math.BigDecimal

/**
 * Баланс счёта — производная от его транзакций: доходы плюс, расходы минус.
 * Доменное правило; формат строк совместим с toCleanDecimal (пробелы-разделители).
 */
fun calculateAccountBalance(transactions: List<TransactionModel>): String =
    transactions
        .fold(BigDecimal.ZERO) { acc, transaction ->
            val amount = transaction.amount.toCleanDecimal()
            if (transaction.isIncome) acc + amount else acc - amount
        }.formatWithSpaces()
