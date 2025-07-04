package com.m4isper.myfinances.domain.repository

import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    val currency: Flow<String>
    fun setCurrency(currencyCode: String)
}