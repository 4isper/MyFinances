package com.m4isper.myfinances.domain.utils

import java.math.BigDecimal
import com.m4isper.myfinances.domain.model.TransactionModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun calculateSumOfTransactions(transactions: List<TransactionModel>): String {
    return transactions.sumOf {
        it.amount.toCleanDecimal()
    }.formatWithSpaces()
}

fun String?.toCleanDecimal(): BigDecimal {
    return try {
        this?.replace("\\s".toRegex(), "")
            ?.toBigDecimal()
            ?: BigDecimal.ZERO
    } catch (e: Exception) {
        BigDecimal.ZERO
    } as BigDecimal
}

fun BigDecimal.formatWithSpaces(): String {
    val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = ' '
    }
    val format = DecimalFormat("#,###", symbols)
    return format.format(this)
}
