package com.m4isper.domain.utils

import com.m4isper.domain.model.TransactionModel
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.collections.sumOf

fun calculateSumOfTransactions(transactions: List<TransactionModel>): String {
    return transactions.sumOf {
        it.amount.toCleanDecimal()
    }.formatWithSpaces()
}

fun String?.toCleanDecimal(): BigDecimal {
    return this?.replace("\\s".toRegex(), "")
        ?.toBigDecimalOrNull()
        ?: BigDecimal.ZERO
}

fun BigDecimal.formatWithSpaces(): String {
    val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = ' '
    }
    val format = DecimalFormat("#,###", symbols)
    return format.format(this)
}
