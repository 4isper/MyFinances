package com.m4isper.myfinances.domain.usecase

import com.m4isper.myfinances.domain.model.AccountModel
import com.m4isper.myfinances.domain.repository.AccountsRepository
import com.m4isper.myfinances.domain.utils.Result
import javax.inject.Inject

class UpdateAccountUseCase @Inject constructor(
    private val repository: AccountsRepository
) {
    suspend operator fun invoke(
        accountId: Int,
        name: String,
        balance: String,
        currency: String
    ): Result<AccountModel> {
        return repository.updateAccount(accountId, name, balance, currency)
    }
}