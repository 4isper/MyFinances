package com.m4isper.myfinances.ui.screens.transactionScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4isper.myfinances.domain.model.TransactionModel
import com.m4isper.myfinances.domain.usecase.GetTransactionByIdUseCase
import com.m4isper.myfinances.domain.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionViewModel (
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val transactionId: Int
) : ViewModel() {
    private val _transaction = MutableStateFlow<TransactionModel?>(null)
    val transaction = _transaction.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadTransactionById() {
        viewModelScope.launch {
            when (val result = getTransactionByIdUseCase(transactionId)) {
                is Result.Success -> {
                    _transaction.value = result.data
                }
                is Result.Failure -> _error.value = result.exception.message
            }
        }
    }
}