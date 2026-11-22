package com.m4isper.analysis

import com.m4isper.domain.usecase.GetExpenseTransactionsUseCase
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

class AnalysisViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadIncomeFillsTransactionsSortedDescending() =
        runTest {
            val repository =
                FakeTransactionRepository(
                    listOf(
                        transactionModel(id = 1, isIncome = true, transactionDate = "2026-11-10T10:00:00Z"),
                        transactionModel(id = 2, isIncome = true, transactionDate = "2026-11-28T10:00:00Z"),
                    ),
                )
            val viewModel =
                AnalysisScreenViewModel(
                    GetIncomeTransactionsUseCase(repository),
                    GetExpenseTransactionsUseCase(repository),
                    FakeCurrencyRepository(),
                )

            viewModel.loadIncome(accountId = 1, start = "2026-11-01", end = "2026-11-30")

            assertEquals(listOf(2, 1), viewModel.transactions.value.map { it.id })
        }

    @Test
    fun loadExpensesSetsErrorWhenRepositoryFails() =
        runTest {
            val repository = FakeTransactionRepository(failingAccountIds = setOf(9))
            val viewModel =
                AnalysisScreenViewModel(
                    GetIncomeTransactionsUseCase(repository),
                    GetExpenseTransactionsUseCase(repository),
                    FakeCurrencyRepository(),
                )

            viewModel.loadExpenses(accountId = 9, start = "2026-11-01", end = "2026-11-30")

            assertTrue(viewModel.error.value != null)
        }
}
