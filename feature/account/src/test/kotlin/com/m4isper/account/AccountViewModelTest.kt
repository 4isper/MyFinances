package com.m4isper.account

import com.m4isper.domain.usecase.GetAccountByIdUseCase
import com.m4isper.domain.usecase.UpdateAccountUseCase
import com.m4isper.testing.FakeAccountsRepository
import com.m4isper.testing.FakeCurrencyRepository
import com.m4isper.testing.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AccountViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadAccountsDoesNotTouchGlobalCurrency() =
        runTest {
            val accountsRepository = FakeAccountsRepository()
            val currencyRepository = FakeCurrencyRepository(initial = "$")
            val viewModel =
                AccountViewModel(
                    GetAccountByIdUseCase(accountsRepository),
                    UpdateAccountUseCase(accountsRepository),
                    currencyRepository,
                )

            viewModel.loadAccounts(1)

            assertNotNull(viewModel.account.value)
            assertEquals("Основной счёт", viewModel.account.value?.name)
            // глобальная валюта (выбор пользователя в KSafe) не перезаписывается из Room
            assertEquals(emptyList<String>(), currencyRepository.setCurrencyCalls)
        }

    @Test
    fun loadAccountsSetsErrorForMissingId() =
        runTest {
            val viewModel =
                AccountViewModel(
                    GetAccountByIdUseCase(FakeAccountsRepository()),
                    UpdateAccountUseCase(FakeAccountsRepository()),
                    FakeCurrencyRepository(),
                )

            viewModel.loadAccounts(42)

            assertNotNull(viewModel.error.value)
            assertEquals(null, viewModel.account.value)
        }

    @Test
    fun updateAccountReturnsTrueAndSyncsCurrencySymbol() =
        runTest {
            val accountsRepository = FakeAccountsRepository()
            val currencyRepository = FakeCurrencyRepository()
            val viewModel =
                AccountViewModel(
                    GetAccountByIdUseCase(accountsRepository),
                    UpdateAccountUseCase(accountsRepository),
                    currencyRepository,
                )

            val result = viewModel.updateAccount(id = 1, name = "Копилка", balance = "500", currency = "$")

            assertTrue(result)
            assertEquals("Копилка", accountsRepository.accounts[1]?.name)
            assertEquals(listOf("$"), currencyRepository.setCurrencyCalls)
        }

    @Test
    fun updateAccountWithUnknownSymbolStillSucceeds() =
        runTest {
            // fallback кода валюты (₽/$/€ → иначе RUB) живёт в реальном репозитории,
            // здесь проверяем только, что VM не блокирует такие значения
            val accountsRepository = FakeAccountsRepository()
            val currencyRepository = FakeCurrencyRepository()
            val viewModel =
                AccountViewModel(
                    GetAccountByIdUseCase(accountsRepository),
                    UpdateAccountUseCase(accountsRepository),
                    currencyRepository,
                )

            val result = viewModel.updateAccount(id = 1, name = "Копилка", balance = "500", currency = "£")

            assertTrue(result)
            assertEquals(listOf("£"), currencyRepository.setCurrencyCalls)
        }
}
