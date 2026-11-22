package com.m4isper.repository

import com.m4isper.domain.repository.CurrencyRepository
import com.m4isper.preferences.CurrencyPreferences
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepositoryImpl
    @Inject
    constructor(
        private val currencyPreferences: CurrencyPreferences,
    ) : CurrencyRepository {
        override val currency: StateFlow<String> = currencyPreferences.currency

        override fun setCurrency(currencyCode: String) {
            currencyPreferences.setCurrency(currencyCode)
        }
    }
