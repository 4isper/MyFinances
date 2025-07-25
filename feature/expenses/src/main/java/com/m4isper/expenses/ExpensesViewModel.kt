package com.m4isper.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4isper.domain.usecase.GetExpenseTransactionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.m4isper.common.Result
import com.m4isper.domain.model.TransactionModel
import com.m4isper.domain.repository.CurrencyRepository

class ExpensesViewModel (
    private val getExpenseTransactionsUseCase: GetExpenseTransactionsUseCase,
    currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _expenses = MutableStateFlow<List<TransactionModel>>(emptyList())
    val expenses = _expenses.asStateFlow()
    val currency = currencyRepository.currency

//    private val _sumOfExpenses = MutableStateFlow(0.0)
//    val sumOfExpenses = _sumOfExpenses.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadExpenses(accountId: Int, start: String, end: String) {
        viewModelScope.launch {
            when (val result = getExpenseTransactionsUseCase(accountId, start, end)) {
                is Result.Success -> {
                    _expenses.value = result.data
//                    _sumOfExpenses.value = calculateSumOfTransactions(result.data)
                }
                is Result.Failure -> _error.value = result.exception.message
            }
        }
    }
}
