package com.m4isper.domain.usecase

import com.m4isper.common.Result
import com.m4isper.domain.model.AccountModel
import com.m4isper.domain.repository.AccountsRepository
import javax.inject.Inject

class GetAccountByIdUseCase @Inject constructor(
    private val accountsRepository: AccountsRepository
) {
    suspend operator fun invoke(accountId: Int): Result<AccountModel> {
        return accountsRepository.getAccountById(accountId)
    }
}