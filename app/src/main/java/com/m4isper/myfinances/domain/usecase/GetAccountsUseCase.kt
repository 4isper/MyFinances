package com.m4isper.myfinances.domain.usecase

import com.m4isper.myfinances.domain.repository.AccountsRepository
import com.m4isper.myfinances.domain.model.AccountModel
import com.m4isper.myfinances.domain.utils.Result
import javax.inject.Inject

class GetAccountsUseCase @Inject constructor(
    private val accountsRepository: AccountsRepository
) {
    suspend operator fun invoke(): Result<List<AccountModel>> {
        return accountsRepository.getAccounts()
    }
}