package com.m4isper.domain.utils

/** Сумма транзакции не распознана как положительное число. */
class InvalidAmountException(
    val rawAmount: String,
) : Exception("Amount must be greater than zero, was: \"$rawAmount\"")
