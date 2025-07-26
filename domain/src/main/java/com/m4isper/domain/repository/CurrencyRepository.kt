package com.m4isper.domain.repository

import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    val currency: Flow<String>
    fun setCurrency(currencyCode: String)
}