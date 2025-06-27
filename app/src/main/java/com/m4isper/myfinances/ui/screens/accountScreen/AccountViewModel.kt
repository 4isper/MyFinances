package com.m4isper.myfinances.ui.screens.accountScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4isper.myfinances.domain.model.AccountModel
import com.m4isper.myfinances.domain.usecase.GetAccountsUseCase
import com.m4isper.myfinances.domain.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getAccountsUseCase: GetAccountsUseCase
) : ViewModel() {
    private val _accounts = MutableStateFlow<List<AccountModel>>(emptyList())
    val accounts = _accounts.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadAccounts() {
        viewModelScope.launch {
            when (val result = getAccountsUseCase()) {
                is Result.Success -> _accounts.value = result.data
                is Result.Failure -> _error.value = result.exception.message
            }
        }
    }
}