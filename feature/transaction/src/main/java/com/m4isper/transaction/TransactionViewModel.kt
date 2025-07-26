package com.m4isper.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4isper.domain.model.AccountModel
import com.m4isper.domain.model.CategoryModel
import com.m4isper.domain.model.TransactionModel
import com.m4isper.domain.repository.CurrencyRepository
import com.m4isper.domain.usecase.AddTransactionUseCase
import com.m4isper.domain.usecase.DeleteTransactionUseCase
import com.m4isper.domain.usecase.GetAccountByIdUseCase
import com.m4isper.domain.usecase.GetCategoriesUseCase
import com.m4isper.domain.usecase.GetTransactionByIdUseCase
import com.m4isper.domain.usecase.UpdateTransactionUseCase
import com.m4isper.domain.BuildConfig
import com.m4isper.common.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionViewModel (
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getAccountByIdUseCase: GetAccountByIdUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    val transactionId: Int,
    currencyRepository: CurrencyRepository
) : ViewModel() {
    private val _transaction = MutableStateFlow<TransactionModel?>(null)
    val transaction = _transaction.asStateFlow()
    val currency = currencyRepository.currency

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

    suspend fun addTransaction(
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String? = null
    ): Boolean{
        return when (val result = addTransactionUseCase(
            accountId = BuildConfig.ID_ACCOUNT,
            categoryId = categoryId,
            amount = amount,
            transactionDate = transactionDate,
            comment = comment
        )) {
            is Result.Success -> true
            is Result.Failure -> {
                _error.value = result.exception.message
                false
            }
        }
    }

    suspend fun updateTransaction(
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String? = null
    ): Boolean {
        return when (val result = updateTransactionUseCase(
            transactionId = transactionId,
            accountId = BuildConfig.ID_ACCOUNT,
            categoryId = categoryId,
            amount = amount,
            transactionDate = transactionDate,
            comment = comment
        )) {
            is Result.Success -> true
            is Result.Failure -> {
                _error.value = result.exception.message
                false
            }
        }
    }

    suspend fun deleteTransaction(): Boolean {
        return when (val result = deleteTransactionUseCase(
            transactionId = transactionId
        )) {
            is Result.Success -> true
            is Result.Failure -> {
                _error.value = result.exception.message
                false
            }
        }
    }

    private val _categories = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categories = _categories.asStateFlow()

    fun loadCategories() {
        viewModelScope.launch {
            when (val result = getCategoriesUseCase()) {
                is Result.Success -> _categories.value = result.data
                is Result.Failure -> _error.value = result.exception.message
            }
        }
    }

    private val _account = MutableStateFlow<AccountModel?>(null)
    val account = _account.asStateFlow()

    fun loadAccount() {
        viewModelScope.launch {
            when (val result = getAccountByIdUseCase(BuildConfig.ID_ACCOUNT)) {
                is Result.Success -> _account.value = result.data
                is Result.Failure -> _error.value = result.exception.message
            }
        }
    }
}