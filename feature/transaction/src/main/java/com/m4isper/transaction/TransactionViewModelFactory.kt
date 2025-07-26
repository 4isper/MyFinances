package com.m4isper.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m4isper.domain.repository.CurrencyRepository
import com.m4isper.domain.usecase.AddTransactionUseCase
import com.m4isper.domain.usecase.DeleteTransactionUseCase
import com.m4isper.domain.usecase.GetAccountByIdUseCase
import com.m4isper.domain.usecase.GetCategoriesUseCase
import com.m4isper.domain.usecase.GetTransactionByIdUseCase
import com.m4isper.domain.usecase.UpdateTransactionUseCase
import com.m4isper.di.TransactionIdQualifier

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