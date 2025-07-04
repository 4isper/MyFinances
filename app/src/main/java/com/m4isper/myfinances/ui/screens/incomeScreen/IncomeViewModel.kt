package com.m4isper.myfinances.ui.screens.incomeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4isper.myfinances.domain.model.TransactionModel
import com.m4isper.myfinances.domain.repository.CurrencyRepository
import com.m4isper.myfinances.domain.usecase.GetIncomeTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.m4isper.myfinances.domain.utils.Result

@HiltViewModel
class IncomeViewModel @Inject constructor(
    private val getIncomeTransactionsUseCase: GetIncomeTransactionsUseCase,
    currencyRepository: CurrencyRepository
) : ViewModel() {
    private val _income = MutableStateFlow<List<TransactionModel>>(emptyList())
    val income = _income.asStateFlow()
    val currency = currencyRepository.currency

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadIncome(accountId: Int, start: String, end: String) {
        viewModelScope.launch {
            when (val result = getIncomeTransactionsUseCase(accountId, start, end)) {
                is Result.Success -> _income.value = result.data
                is Result.Failure -> _error.value = result.exception.message
            }
        }
    }
}
