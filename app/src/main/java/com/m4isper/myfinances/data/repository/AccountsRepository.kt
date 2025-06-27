package com.m4isper.myfinances.data.repository

import com.m4isper.myfinances.domain.model.AccountModel
import com.m4isper.myfinances.domain.utils.Result

interface AccountsRepository {
    suspend fun getAccounts(): Result<List<AccountModel>>
}