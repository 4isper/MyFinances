package com.m4isper.income

import com.m4isper.domain.usecase.DeleteTransactionUseCase
import com.m4isper.domain.usecase.GetIncomeTransactionsUseCase
import com.m4isper.testing.FakeCurrencyRepository
import com.m4isper.testing.FakeTransactionRepository
import com.m4isper.testing.MainDispatcherRule
import com.m4isper.testing.transactionModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class IncomeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadIncomeFillsStateWithIncomeOnly() =
        runTest {
            val repository =
                FakeTransactionRepository(
                    listOf(
                        transactionModel(id = 1, isIncome = true, amount = "9000", categoryName = "Зарплата"),
                        transactionModel(id = 2, isIncome = false, amount = "300"),
                    ),
                )
            val viewModel =
                IncomeViewModel(
                    GetIncomeTransactionsUseCase(repository),
                    DeleteTransactionUseCase(repository),
                    FakeCurrencyRepository(),
                )

            viewModel.loadIncome(accountId = 1, start = "2026-11-22", end = "2026-11-22")

            assertEquals(1, viewModel.income.value.size)
            assertEquals(
                "Зарплата",
                viewModel.income.value
                    .first()
                    .categoryName,
            )
            assertEquals(null, viewModel.error.value)
        }

    @Test
    fun deleteTransactionRemovesAndReloadsSamePeriod() =
        runTest {
            val repository =
                FakeTransactionRepository(
                    listOf(
                        transactionModel(id = 1, isIncome = true, amount = "9000", categoryName = "Зарплата"),
                        transactionModel(id = 2, isIncome = true, amount = "500", transactionDate = "2026-11-22T11:00:00Z"),
                    ),
                )
            val viewModel =
                IncomeViewModel(
                    GetIncomeTransactionsUseCase(repository),
                    DeleteTransactionUseCase(repository),
                    FakeCurrencyRepository(),
                )
            viewModel.loadIncome(accountId = 1, start = "2026-11-22", end = "2026-11-22")

            viewModel.deleteTransaction(1)

            assertEquals(1, viewModel.income.value.size)
            assertEquals(
                2,
                viewModel.income.value
                    .first()
                    .id,
            )
            assertTrue(repository.transactions.none { it.id == 1 })
        }
}
