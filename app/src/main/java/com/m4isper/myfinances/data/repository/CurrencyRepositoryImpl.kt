package com.m4isper.myfinances.data.repository

import com.m4isper.myfinances.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepositoryImpl @Inject constructor() : CurrencyRepository {

    private val _currency = MutableStateFlow("₽")
    override val currency: StateFlow<String> = _currency

    override fun setCurrency(currencyCode: String) {
        _currency.value = currencyCode
    }
}