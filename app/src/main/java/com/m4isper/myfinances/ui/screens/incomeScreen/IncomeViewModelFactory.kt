package com.m4isper.myfinances.ui.screens.incomeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m4isper.myfinances.domain.repository.CurrencyRepository
import com.m4isper.myfinances.domain.usecase.GetIncomeTransactionsUseCase
import javax.inject.Inject

class IncomeViewModelFactory @Inject constructor(
    private val getIncomeTransactionsUseCase: GetIncomeTransactionsUseCase,
    private val currencyRepository: CurrencyRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IncomeViewModel::class.java)) {
            return IncomeViewModel(
                getIncomeTransactionsUseCase,
                currencyRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}