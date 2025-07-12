package com.m4isper.myfinances.ui.screens.accountScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m4isper.myfinances.domain.repository.CurrencyRepository
import com.m4isper.myfinances.domain.usecase.GetAccountByIdUseCase
import com.m4isper.myfinances.domain.usecase.UpdateAccountUseCase
import javax.inject.Inject

class AccountViewModelFactory @Inject constructor(
    private val getAccountByIdUseCase: GetAccountByIdUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val currencyRepository: CurrencyRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            return AccountViewModel(
                getAccountByIdUseCase,
                updateAccountUseCase,
                currencyRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}