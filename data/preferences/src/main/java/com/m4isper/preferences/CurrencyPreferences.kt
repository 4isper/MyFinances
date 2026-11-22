package com.m4isper.preferences

import android.content.Context
import eu.anifantakis.lib.ksafe.KSafe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CurrencyPreferences(context: Context) {
    private val ksafe = KSafe(context)

    private val _currency = MutableStateFlow(ksafe.getDirect("currency", "₽"))
    val currency: StateFlow<String> = _currency.asStateFlow()

    fun setCurrency(symbol: String) {
        _currency.value = symbol
        ksafe.putDirect("currency", symbol)
    }
}
