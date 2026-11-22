package com.m4isper.domain

import com.m4isper.common.Result
import com.m4isper.domain.model.AccountModel
import com.m4isper.domain.usecase.GetAccountByIdUseCase
import com.m4isper.domain.usecase.GetAccountsUseCase
import com.m4isper.domain.usecase.UpdateAccountUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AccountUseCasesTest {
    private val repository = FakeAccountsRepository()

    @Test
    fun `getAccounts returns seeded account`() =
        runTest {
            val result = GetAccountsUseCase(repository)()

            assertTrue(result is Result.Success)
            val accounts = (result as Result.Success).data
            assertEquals(1, accounts.size)
            assertEquals("Основной счёт", accounts.first().name)
        }

    @Test
    fun `getAccountById returns account by id`() =
        runTest {
            val result = GetAccountByIdUseCase(repository)(1)

            assertTrue(result is Result.Success)
            assertEquals("₽", (result as Result.Success).data.currency)
        }

    @Test
    fun `getAccountById fails for missing id`() =
        runTest {
            val result = GetAccountByIdUseCase(repository)(42)

            assertTrue(result is Result.Failure)
        }

    @Test
    fun `updateAccount replaces account fields`() =
        runTest {
            val result = UpdateAccountUseCase(repository)(accountId = 1, name = "Копилка", balance = "500", currency = "$")

            assertTrue(result is Result.Success)
            assertEquals(AccountModel(id = 1, name = "Копилка", balance = "500", currency = "$"), repository.accounts[1])
        }
}
