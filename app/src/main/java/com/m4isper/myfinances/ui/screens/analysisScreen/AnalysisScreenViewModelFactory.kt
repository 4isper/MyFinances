package com.m4isper.myfinances.ui.screens.analysisScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m4isper.myfinances.domain.repository.CurrencyRepository
import com.m4isper.myfinances.domain.usecase.GetExpenseTransactionsUseCase
import com.m4isper.myfinances.domain.usecase.GetIncomeTransactionsUseCase
import javax.inject.Inject

class AnalysisScreenViewModelFactory @Inject constructor(
    private val getIncomeTransactionsUseCase: GetIncomeTransactionsUseCase,
    private val getExpenseTransactionsUseCase: GetExpenseTransactionsUseCase,
    private val currencyRepository: CurrencyRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnalysisScreenViewModel::class.java))
            return AnalysisScreenViewModel(
                getIncomeTransactionsUseCase,
                getExpenseTransactionsUseCase,
                currencyRepository
            ) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}