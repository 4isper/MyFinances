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
