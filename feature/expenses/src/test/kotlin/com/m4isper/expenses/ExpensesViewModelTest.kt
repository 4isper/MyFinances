package com.m4isper.expenses

import com.m4isper.domain.usecase.GetExpenseTransactionsUseCase
import com.m4isper.testing.FakeCurrencyRepository
import com.m4isper.testing.FakeTransactionRepository
import com.m4isper.testing.MainDispatcherRule
import com.m4isper.testing.transactionModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

class ExpensesViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadExpensesFillsStateWithTodayExpenses() =
        runTest {
            val repository =
                FakeTransactionRepository(
                    listOf(
                        transactionModel(id = 1, isIncome = false, amount = "500"),
                        transactionModel(id = 2, isIncome = false, amount = "700", transactionDate = "2026-11-23T10:00:00Z"),
                    ),
                )
            val viewModel = ExpensesViewModel(GetExpenseTransactionsUseCase(repository), FakeCurrencyRepository())

            viewModel.loadExpenses(accountId = 1, start = "2026-11-22", end = "2026-11-22")

            assertEquals(1, viewModel.expenses.value.size)
            assertEquals(
                "500",
                viewModel.expenses.value
                    .first()
                    .amount,
            )
            assertEquals(null, viewModel.error.value)
        }

    @Test
    fun loadExpensesSetsErrorWhenRepositoryFails() =
        runTest {
            val repository = FakeTransactionRepository(failingAccountIds = setOf(9))
            val viewModel = ExpensesViewModel(GetExpenseTransactionsUseCase(repository), FakeCurrencyRepository())

            viewModel.loadExpenses(accountId = 9, start = "2026-11-22", end = "2026-11-22")

            assertNotNull(viewModel.error.value)
            assertEquals(0, viewModel.expenses.value.size)
        }
}
