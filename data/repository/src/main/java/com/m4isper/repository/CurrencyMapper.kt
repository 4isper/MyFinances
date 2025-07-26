package com.m4isper.repository

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