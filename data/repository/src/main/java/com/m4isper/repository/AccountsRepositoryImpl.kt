package com.m4isper.repository

import com.m4isper.common.safeApiCall
import com.m4isper.domain.model.AccountModel
import com.m4isper.domain.repository.AccountsRepository
import com.m4isper.remote.AccountDto
import com.m4isper.remote.SHMRFinanceApi
import com.m4isper.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountsRepositoryImpl @Inject constructor(
    private val api: SHMRFinanceApi
) : AccountsRepository {
    override suspend fun getAccounts(): Result<List<AccountModel>> = withContext(Dispatchers.IO) {
        val result = safeApiCall {
            api.getAccounts()
        }

        when (result) {
            is Result.Success -> {
                val data = result.data.map { it.toDomain()}
                Result.Success(data)
            }
            is Result.Failure -> result
        }
    }

    override suspend fun getAccountById(accountId: Int): Result<AccountModel> = withContext(Dispatchers.IO) {
        val result = safeApiCall {
            api.getAccountById(accountId)
        }

        when (result) {
            is Result.Success -> {
                val data = result.data.toDomain()
                Result.Success(data)
            }
            is Result.Failure -> result
        }
    }

    override suspend fun updateAccount(
        accountId: Int,
        name: String,
        balance: String,
        currency: String
    ): Result<AccountModel> = withContext(Dispatchers.IO) {
        val dto = AccountDto(
            id = accountId,
            name = name,
            balance = balance,
            currency = currency
        )

        val result = safeApiCall {
            api.updateAccount(accountId, dto)
        }

        when (result) {
            is Result.Success -> {
                val data = result.data.toDomain()
                Result.Success(data)
            }
            is Result.Failure -> result
        }
    }
}