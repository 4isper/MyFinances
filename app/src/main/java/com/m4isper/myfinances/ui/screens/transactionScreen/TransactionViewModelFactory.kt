package com.m4isper.myfinances.ui.screens.transactionScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m4isper.myfinances.di.TransactionIdQualifier
import com.m4isper.myfinances.domain.repository.CurrencyRepository
import com.m4isper.myfinances.domain.usecase.AddTransactionUseCase
import com.m4isper.myfinances.domain.usecase.DeleteTransactionUseCase
import com.m4isper.myfinances.domain.usecase.GetAccountByIdUseCase
import com.m4isper.myfinances.domain.usecase.GetCategoriesUseCase
import com.m4isper.myfinances.domain.usecase.GetTransactionByIdUseCase
import com.m4isper.myfinances.domain.usecase.UpdateTransactionUseCase
import javax.inject.Inject

class TransactionViewModelFactory @Inject constructor(
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getAccountByIdUseCase: GetAccountByIdUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    @TransactionIdQualifier private val transactionId: Int,
    private val currencyRepository: CurrencyRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(
                getTransactionByIdUseCase,
                addTransactionUseCase,
                getCategoriesUseCase,
                getAccountByIdUseCase,
                updateTransactionUseCase,
                deleteTransactionUseCase,
                transactionId,
                currencyRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}