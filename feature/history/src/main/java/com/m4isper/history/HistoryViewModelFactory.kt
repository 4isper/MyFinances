package com.m4isper.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m4isper.domain.repository.CurrencyRepository
import com.m4isper.domain.usecase.GetExpenseTransactionsUseCase
import com.m4isper.domain.usecase.GetIncomeTransactionsUseCase
import javax.inject.Inject

class HistoryViewModelFactory @Inject constructor(
    private val getIncomeTransactionsUseCase: GetIncomeTransactionsUseCase,
    private val getExpenseTransactionsUseCase: GetExpenseTransactionsUseCase,
    private val currencyRepository: CurrencyRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java))
            return HistoryViewModel(
                getIncomeTransactionsUseCase,
                getExpenseTransactionsUseCase,
                currencyRepository
            ) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
