package com.m4isper.domain.repository

import com.m4isper.domain.model.AccountModel
import com.m4isper.common.Result

interface AccountsRepository {
    suspend fun getAccounts(): Result<List<AccountModel>>

    suspend fun getAccountById(
        accountId: Int
    ): Result<AccountModel>

    suspend fun updateAccount(
        accountId: Int,
        name: String,
        balance: String,
        currency: String
    ): Result<AccountModel>
}