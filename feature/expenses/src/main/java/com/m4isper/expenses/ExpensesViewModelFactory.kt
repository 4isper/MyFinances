package com.m4isper.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m4isper.domain.repository.CurrencyRepository
import com.m4isper.domain.usecase.GetExpenseTransactionsUseCase
import javax.inject.Inject

class ExpensesViewModelFactory @Inject constructor(
    private val getExpenseTransactionsUseCase: GetExpenseTransactionsUseCase,
    private val currencyRepository: CurrencyRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpensesViewModel::class.java)) {
            return ExpensesViewModel(
                getExpenseTransactionsUseCase,
                currencyRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
