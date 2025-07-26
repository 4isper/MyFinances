package com.m4isper.domain.usecase

import com.m4isper.common.Result
import com.m4isper.domain.model.AccountModel
import com.m4isper.domain.repository.AccountsRepository
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