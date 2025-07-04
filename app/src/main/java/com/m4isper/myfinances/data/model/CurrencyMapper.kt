package com.m4isper.myfinances.data.model

fun String.toCurrencySymbol(): String = when (this) {
    "RUB" -> "₽"
    "USD" -> "$"
    "EUR" -> "€"
    else -> "₽"
}

fun String.toCurrencyCode(): String = when (this) {
    "₽" -> "RUB"
    "$" -> "USD"
    "€" -> "EUR"
    else -> "RUB"
}