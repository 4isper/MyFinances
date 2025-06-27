package com.m4isper.myfinances.ui.screens.expensesScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4isper.myfinances.domain.model.TransactionModel
import com.m4isper.myfinances.domain.usecase.GetExpenseTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.m4isper.myfinances.domain.utils.Result

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val getExpenseTransactionsUseCase: GetExpenseTransactionsUseCase
) : ViewModel() {

    private val _expenses = MutableStateFlow<List<TransactionModel>>(emptyList())
    val expenses = _expenses.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadExpenses(accountId: Int, start: String, end: String) {
        viewModelScope.launch {
            when (val result = getExpenseTransactionsUseCase(accountId, start, end)) {
                is Result.Success -> _expenses.value = result.data
                is Result.Failure -> _error.value = result.exception.message
            }
        }
    }
}
