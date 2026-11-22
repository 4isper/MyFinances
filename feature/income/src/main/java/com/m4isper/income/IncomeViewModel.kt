package com.m4isper.income

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4isper.common.Result
import com.m4isper.domain.model.TransactionModel
import com.m4isper.domain.repository.CurrencyRepository
import com.m4isper.domain.usecase.DeleteTransactionUseCase
import com.m4isper.domain.usecase.GetIncomeTransactionsUseCase
import com.m4isper.ui.components.toErrorRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class IncomeViewModel(
    private val getIncomeTransactionsUseCase: GetIncomeTransactionsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    currencyRepository: CurrencyRepository,
) : ViewModel() {
    private val _income = MutableStateFlow<List<TransactionModel>>(emptyList())
    val income = _income.asStateFlow()
    val currency = currencyRepository.currency

    private val _error = MutableStateFlow<Int?>(null)
    val error = _error.asStateFlow()

    // параметры последней загрузки — чтобы после свайп-удаления перезагрузить тот же период
    private var lastAccountId = 0
    private var lastStart = ""
    private var lastEnd = ""

    fun loadIncome(
        accountId: Int,
        start: String,
        end: String,
    ) {
        lastAccountId = accountId
        lastStart = start
        lastEnd = end
        viewModelScope.launch {
            when (val result = getIncomeTransactionsUseCase(accountId, start, end)) {
                is Result.Success -> _income.value = result.data
                is Result.Failure -> _error.value = result.exception.toErrorRes()
            }
        }
    }

    fun deleteTransaction(transactionId: Int) {
        viewModelScope.launch {
            when (val result = deleteTransactionUseCase(transactionId)) {
                is Result.Success -> loadIncome(lastAccountId, lastStart, lastEnd)
                is Result.Failure -> _error.value = result.exception.toErrorRes()
            }
        }
    }
}
