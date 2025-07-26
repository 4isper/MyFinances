package com.m4isper.domain.usecase

import com.m4isper.domain.model.AccountModel
import com.m4isper.domain.repository.AccountsRepository
import com.m4isper.common.Result
import javax.inject.Inject

class GetAccountsUseCase @Inject constructor(
    private val accountsRepository: AccountsRepository
) {
    suspend operator fun invoke(): Result<List<AccountModel>> {
        return accountsRepository.getAccounts()
    }
}