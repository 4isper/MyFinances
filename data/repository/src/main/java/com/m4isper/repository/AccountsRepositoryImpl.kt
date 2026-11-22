package com.m4isper.repository

import com.m4isper.common.Result
import com.m4isper.common.safeCall
import com.m4isper.domain.AppConfig
import com.m4isper.domain.model.AccountModel
import com.m4isper.domain.repository.AccountsRepository
import com.m4isper.local.dao.AccountDao
import com.m4isper.local.entity.AccountEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountsRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao
) : AccountsRepository {

    override suspend fun getAccounts(): Result<List<AccountModel>> = withContext(Dispatchers.IO) {
        safeCall {
            ensureAccountExists()
            accountDao.getAll().map { it.toModel() }
        }
    }

    override suspend fun getAccountById(accountId: Int): Result<AccountModel> = withContext(Dispatchers.IO) {
        safeCall {
            ensureAccountExists()
            accountDao.getById(accountId)?.toModel()
                ?: throw NoSuchElementException("Счёт $accountId не найден")
        }
    }

    override suspend fun updateAccount(
        accountId: Int,
        name: String,
        balance: String,
        currency: String
    ): Result<AccountModel> = withContext(Dispatchers.IO) {
        safeCall {
            val entity = AccountEntity(
                id = accountId,
                name = name,
                balance = balance,
                currency = currency.toCurrencyCode()
            )
            accountDao.upsert(entity)
            entity.toModel()
        }
    }

    private suspend fun ensureAccountExists() {
        if (accountDao.count() == 0) {
            accountDao.upsert(
                AccountEntity(
                    id = AppConfig.LOCAL_ACCOUNT_ID,
                    name = "Основной счёт",
                    balance = "0",
                    currency = "RUB"
                )
            )
        }
    }
}
