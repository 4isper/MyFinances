package com.m4isper.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4isper.domain.model.AccountModel
import com.m4isper.domain.repository.CurrencyRepository
import com.m4isper.domain.usecase.GetAccountByIdUseCase
import com.m4isper.domain.usecase.UpdateAccountUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.m4isper.common.Result

class AccountViewModel (
    private val getAccountByIdUseCase: GetAccountByIdUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val currencyRepository: CurrencyRepository
) : ViewModel() {
    private val _account = MutableStateFlow<AccountModel?>(null)
    val account = _account.asStateFlow()
    val currency = currencyRepository.currency

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadAccounts(accountId: Int) {
        viewModelScope.launch {
            when (val result = getAccountByIdUseCase(accountId)) {
                is Result.Success -> {
                    _account.value = result.data
                    account.value?.let { currencyRepository.setCurrency(it.currency) }
                }
                is Result.Failure -> _error.value = result.exception.message
            }
        }
    }

    suspend fun updateAccount(id: Int, name: String, balance: String, currency: String): Boolean {
        val currencyCode = when (currency) {
            "₽" -> "RUB"
            "$" -> "USD"
            "€" -> "EUR"
            else -> "UNKNOWN"
        }

        return when (val result = updateAccountUseCase(id, name, balance, currencyCode)) {
            is Result.Success -> {
                currencyRepository.setCurrency(currencyCode)
                true
            }
            is Result.Failure -> {
                _error.value = result.exception.message
                false
            }
        }
    }
}