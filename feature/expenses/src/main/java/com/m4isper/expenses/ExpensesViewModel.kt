package com.m4isper.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4isper.common.Result
import com.m4isper.domain.model.TransactionModel
import com.m4isper.domain.repository.CurrencyRepository
import com.m4isper.domain.usecase.DeleteTransactionUseCase
import com.m4isper.domain.usecase.GetExpenseTransactionsUseCase
import com.m4isper.ui.components.toErrorRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExpensesViewModel(
    private val getExpenseTransactionsUseCase: GetExpenseTransactionsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    currencyRepository: CurrencyRepository,
) : ViewModel() {
    private val _expenses = MutableStateFlow<List<TransactionModel>>(emptyList())
    val expenses = _expenses.asStateFlow()
    val currency = currencyRepository.currency

    private val _error = MutableStateFlow<Int?>(null)
    val error = _error.asStateFlow()

    // параметры последней загрузки — чтобы после свайп-удаления перезагрузить тот же период
    private var lastAccountId = 0
    private var lastStart = ""
    private var lastEnd = ""

    fun loadExpenses(
        accountId: Int,
        start: String,
        end: String,
    ) {
        lastAccountId = accountId
        lastStart = start
        lastEnd = end
        viewModelScope.launch {
            when (val result = getExpenseTransactionsUseCase(accountId, start, end)) {
                is Result.Success -> {
                    _expenses.value = result.data
                }
                is Result.Failure -> _error.value = result.exception.toErrorRes()
            }
        }
    }

    fun deleteTransaction(transactionId: Int) {
        viewModelScope.launch {
            when (val result = deleteTransactionUseCase(transactionId)) {
                is Result.Success -> loadExpenses(lastAccountId, lastStart, lastEnd)
                is Result.Failure -> _error.value = result.exception.toErrorRes()
            }
        }
    }
}
