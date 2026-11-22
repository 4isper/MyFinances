package com.m4isper.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4isper.common.DateUtils.parseDate
import com.m4isper.common.Result
import com.m4isper.domain.model.TransactionModel
import com.m4isper.domain.repository.CurrencyRepository
import com.m4isper.domain.usecase.GetExpenseTransactionsUseCase
import com.m4isper.domain.usecase.GetIncomeTransactionsUseCase
import com.m4isper.ui.components.toErrorRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val getIncomeTransactionsUseCase: GetIncomeTransactionsUseCase,
    private val getExpenseTransactionsUseCase: GetExpenseTransactionsUseCase,
    currencyRepository: CurrencyRepository,
) : ViewModel() {
    private val _transactions = MutableStateFlow<List<TransactionModel>>(emptyList())
    val transactions = _transactions.asStateFlow()
    val currency = currencyRepository.currency

    private val _error = MutableStateFlow<Int?>(null)
    val error = _error.asStateFlow()

    fun loadIncome(
        accountId: Int,
        start: String,
        end: String,
    ) {
        viewModelScope.launch {
            when (val result = getIncomeTransactionsUseCase(accountId, start, end)) {
                is Result.Success -> {
                    val sorted = result.data.sortedByDescending { parseDate(it.transactionDate) }
                    _transactions.value = sorted
                }
                is Result.Failure -> _error.value = result.exception.toErrorRes()
            }
        }
    }

    fun loadExpenses(
        accountId: Int,
        start: String,
        end: String,
    ) {
        viewModelScope.launch {
            when (val result = getExpenseTransactionsUseCase(accountId, start, end)) {
                is Result.Success -> {
                    val sorted = result.data.sortedByDescending { parseDate(it.transactionDate) }
                    _transactions.value = sorted
                }
                is Result.Failure -> _error.value = result.exception.toErrorRes()
            }
        }
    }
}
