package com.m4isper.repository

import com.m4isper.common.Result
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AccountsRepositoryImplTest {
    private lateinit var accountDao: FakeAccountDao
    private lateinit var repository: AccountsRepositoryImpl

    @Before
    fun setUp() {
        accountDao = FakeAccountDao()
        repository = AccountsRepositoryImpl(accountDao)
    }

    @Test
    fun getAccountsSeedsDefaultAccountWhenTableEmpty() =
        runTest {
            val result = repository.getAccounts()

            val data = (result as Result.Success).data
            assertEquals(1, data.size)
            assertEquals(1, data[0].id)
            assertEquals("Основной счёт", data[0].name)
            assertEquals("0", data[0].balance)
            assertEquals("₽", data[0].currency)
        }

    @Test
    fun seedIsIdempotent() =
        runTest {
            repository.getAccounts()
            repository.getAccounts()

            assertEquals(1, accountDao.rows.size)
        }

    @Test
    fun getAccountsDoesNotOverwriteUpdatedAccount() =
        runTest {
            repository.updateAccount(1, "Копилка", "500", "$")

            val data = (repository.getAccounts() as Result.Success).data

            assertEquals(1, data.size)
            assertEquals("Копилка", data[0].name)
            assertEquals("500", data[0].balance)
            assertEquals("$", data[0].currency)
        }

    @Test
    fun getAccountByIdFailsForMissingId() =
        runTest {
            val result = repository.getAccountById(42)

            assertTrue(result is Result.Failure)
        }

    @Test
    fun updateAccountConvertsSymbolToCurrencyCode() =
        runTest {
            val result = repository.updateAccount(1, "Копилка", "500", "$")

            assertTrue(result is Result.Success)
            // в Room хранится код валюты, наружу отдаётся символ
            assertEquals("USD", accountDao.rows.first { it.id == 1 }.currency)
            assertEquals("$", (result as Result.Success).data.currency)
        }
}
