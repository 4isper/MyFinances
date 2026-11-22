package com.m4isper.history

import com.m4isper.common.DateUtils
import com.m4isper.domain.usecase.GetExpenseTransactionsUseCase
import com.m4isper.domain.usecase.GetIncomeTransactionsUseCase
import com.m4isper.testing.FakeCurrencyRepository
import com.m4isper.testing.FakeTransactionRepository
import com.m4isper.testing.MainDispatcherRule
import com.m4isper.testing.transactionModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HistoryViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadIncomeSortsTransactionsByDateDescending() =
        runTest {
            val repository =
                FakeTransactionRepository(
                    listOf(
                        transactionModel(id = 1, isIncome = true, transactionDate = "2026-11-20T10:00:00Z"),
                        transactionModel(id = 2, isIncome = true, transactionDate = "2026-11-25T10:00:00Z"),
                        transactionModel(id = 3, isIncome = true, transactionDate = "2026-11-22T10:00:00Z"),
                    ),
                )
            val viewModel =
                HistoryViewModel(
                    GetIncomeTransactionsUseCase(repository),
                    GetExpenseTransactionsUseCase(repository),
                    FakeCurrencyRepository(),
                )

            viewModel.loadIncome(accountId = 1, start = "2026-11-01", end = "2026-11-30")

            val dates = viewModel.transactions.value.map { DateUtils.extractDate(it.transactionDate).toString() }
            assertEquals(listOf("2026-11-25", "2026-11-22", "2026-11-20"), dates)
        }

    @Test
    fun loadExpensesReplacesPreviousTransactions() =
        runTest {
            val repository =
                FakeTransactionRepository(
                    listOf(transactionModel(id = 1, isIncome = false, transactionDate = "2026-11-22T10:00:00Z")),
                )
            val viewModel =
                HistoryViewModel(
                    GetIncomeTransactionsUseCase(repository),
                    GetExpenseTransactionsUseCase(repository),
                    FakeCurrencyRepository(),
                )
            viewModel.loadIncome(accountId = 1, start = "2026-11-01", end = "2026-11-30")

            viewModel.loadExpenses(accountId = 1, start = "2026-11-01", end = "2026-11-30")

            assertEquals(1, viewModel.transactions.value.size)
            assertEquals(
                false,
                viewModel.transactions.value
                    .first()
                    .isIncome,
            )
        }
}
