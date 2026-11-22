package com.m4isper.domain

import com.m4isper.common.Result
import com.m4isper.domain.utils.InvalidAmountException
import com.m4isper.domain.usecase.AddTransactionUseCase
import com.m4isper.domain.usecase.DeleteTransactionUseCase
import com.m4isper.domain.usecase.GetExpenseTransactionsUseCase
import com.m4isper.domain.usecase.GetIncomeTransactionsUseCase
import com.m4isper.domain.usecase.GetTransactionByIdUseCase
import com.m4isper.domain.usecase.GetTransactionsUseCase
import com.m4isper.domain.usecase.UpdateTransactionUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TransactionUseCasesTest {
    private val repository =
        FakeTransactionRepository(
            listOf(
                transactionModel(id = 1, isIncome = false, amount = "1000"),
                transactionModel(id = 2, isIncome = true, amount = "5000", categoryName = "Зарплата"),
            ),
        )

    @Test
    fun `addTransaction delegates to repository and returns created transaction`() =
        runTest {
            val useCase = AddTransactionUseCase(repository)

            val result = useCase(accountId = 1, categoryId = 4, amount = "700", transactionDate = "2026-11-22T09:00:00Z", comment = null)

            assertTrue(result is Result.Success)
            assertEquals("700", (result as Result.Success).data.amount)
            assertEquals(1, repository.added.size)
            assertEquals(3, repository.transactions.size)
        }

    @Test
    fun `addTransaction fails for non-positive or garbage amount`() =
        runTest {
            val useCase = AddTransactionUseCase(repository)

            for (amount in listOf("0", "-5", "abc", "")) {
                val result = useCase(accountId = 1, categoryId = 4, amount = amount, transactionDate = "2026-11-22T09:00:00Z", comment = null)
                assertTrue((result as Result.Failure).exception is InvalidAmountException)
            }
            assertEquals(0, repository.added.size)
        }

    @Test
    fun `updateTransaction fails for non-positive amount`() =
        runTest {
            val useCase = UpdateTransactionUseCase(repository)

            val result = useCase(transactionId = 1, accountId = 1, categoryId = 4, amount = "0", transactionDate = "2026-11-22T09:00:00Z", comment = null)

            assertTrue((result as Result.Failure).exception is InvalidAmountException)
        }

    @Test
    fun `updateTransaction delegates to repository`() =
        runTest {
            val useCase = UpdateTransactionUseCase(repository)

            val result =
                useCase(
                    transactionId = 1,
                    accountId = 1,
                    categoryId = 4,
                    amount = "2000",
                    transactionDate = "2026-11-22T09:00:00Z",
                    comment = "покупка",
                )

            assertTrue(result is Result.Success)
            assertEquals("2000", repository.transactions[0].amount)
            assertEquals("покупка", repository.transactions[0].comment)
        }

    @Test
    fun `updateTransaction fails for missing transaction`() =
        runTest {
            val useCase = UpdateTransactionUseCase(repository)

            val result =
                useCase(
                    transactionId = 999,
                    accountId = 1,
                    categoryId = 4,
                    amount = "1",
                    transactionDate = "2026-11-22T09:00:00Z",
                    comment = null,
                )

            assertTrue(result is Result.Failure)
        }

    @Test
    fun `deleteTransaction delegates to repository`() =
        runTest {
            val useCase = DeleteTransactionUseCase(repository)

            val result = useCase(1)

            assertEquals(Result.Success(Unit), result)
            assertEquals(listOf(1), repository.deletedIds)
            assertTrue(repository.transactions.none { it.id == 1 })
        }

    @Test
    fun `getTransactionById returns transaction`() =
        runTest {
            val useCase = GetTransactionByIdUseCase(repository)

            val result = useCase(2)

            assertTrue(result is Result.Success)
            assertEquals("Зарплата", (result as Result.Success).data.categoryName)
        }

    @Test
    fun `getTransactions filters by accountId`() =
        runTest {
            repository.transactions.add(transactionModel(id = 3, accountId = 2))
            val useCase = GetTransactionsUseCase(repository)

            val result = useCase(accountId = 1, startDate = "2026-01-01", endDate = "2026-12-31")

            val data = (result as Result.Success).data
            assertTrue(data.all { it.accountId == 1 })
            assertEquals(2, data.size)
        }

    @Test
    fun `getIncomeTransactions returns only income`() =
        runTest {
            val useCase = GetIncomeTransactionsUseCase(repository)

            val data = (useCase(accountId = 1, startDate = "2026-01-01", endDate = "2026-12-31") as Result.Success).data

            assertEquals(1, data.size)
            assertTrue(data.first().isIncome)
        }

    @Test
    fun `getExpenseTransactions returns only expenses`() =
        runTest {
            val useCase = GetExpenseTransactionsUseCase(repository)

            val data = (useCase(accountId = 1, startDate = "2026-01-01", endDate = "2026-12-31") as Result.Success).data

            assertEquals(1, data.size)
            assertTrue(!data.first().isIncome)
        }
}
