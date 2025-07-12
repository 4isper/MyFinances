package com.m4isper.myfinances.ui.screens.transactionScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m4isper.myfinances.di.TransactionIdQualifier
import com.m4isper.myfinances.domain.usecase.GetTransactionByIdUseCase
import javax.inject.Inject

class TransactionViewModelFactory @Inject constructor(
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    @TransactionIdQualifier private val transactionId: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(
                getTransactionByIdUseCase,
                transactionId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}