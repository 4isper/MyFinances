package com.m4isper.transaction

import com.m4isper.domain.model.CategoryModel
import com.m4isper.domain.usecase.AddTransactionUseCase
import com.m4isper.domain.usecase.DeleteTransactionUseCase
import com.m4isper.domain.usecase.GetAccountByIdUseCase
import com.m4isper.domain.usecase.GetCategoriesUseCase
import com.m4isper.domain.usecase.GetTransactionByIdUseCase
import com.m4isper.domain.usecase.UpdateTransactionUseCase
import com.m4isper.testing.FakeAccountsRepository
import com.m4isper.testing.FakeCategoriesRepository
import com.m4isper.testing.FakeCurrencyRepository
import com.m4isper.testing.FakeTransactionRepository
import com.m4isper.testing.MainDispatcherRule
import com.m4isper.testing.transactionModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class TransactionViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun viewModel(
        repository: FakeTransactionRepository,
        transactionId: Int = -1,
    ): TransactionViewModel =
        TransactionViewModel(
            getTransactionByIdUseCase = GetTransactionByIdUseCase(repository),
            addTransactionUseCase = AddTransactionUseCase(repository),
            getCategoriesUseCase =
                GetCategoriesUseCase(
                    FakeCategoriesRepository(listOf(CategoryModel(id = 4, name = "Продукты", emoji = "🛒", isIncome = false))),
                ),
            getAccountByIdUseCase = GetAccountByIdUseCase(FakeAccountsRepository()),
            updateTransactionUseCase = UpdateTransactionUseCase(repository),
            deleteTransactionUseCase = DeleteTransactionUseCase(repository),
            transactionId = transactionId,
            currencyRepository = FakeCurrencyRepository(),
        )

    @Test
    fun loadAccountAndCategoriesFillStates() =
        runTest {
            val viewModel = viewModel(FakeTransactionRepository())

            viewModel.loadAccount()
            viewModel.loadCategories()

            assertEquals("Основной счёт", viewModel.account.value?.name)
            assertEquals(1, viewModel.categories.value.size)
        }

    @Test
    fun loadTransactionByIdFillsTransactionState() =
        runTest {
            val repository = FakeTransactionRepository(listOf(transactionModel(id = 7, amount = "900")))
            val viewModel = viewModel(repository, transactionId = 7)

            viewModel.loadTransactionById()

            assertEquals("900", viewModel.transaction.value?.amount)
        }

    @Test
    fun addTransactionReturnsTrueAndStoresTransaction() =
        runTest {
            val repository = FakeTransactionRepository()
            val viewModel = viewModel(repository)

            val result =
                viewModel.addTransaction(
                    categoryId = 4,
                    amount = "1000",
                    transactionDate = "2026-11-22T10:00:00Z",
                    comment = "тест",
                )

            assertTrue(result)
            assertEquals(1, repository.transactions.size)
            assertEquals("1000", repository.transactions.first().amount)
            assertEquals(null, viewModel.error.value)
        }

    @Test
    fun addTransactionRejectsNonPositiveAmount() =
        runTest {
            val repository = FakeTransactionRepository()
            val viewModel = viewModel(repository)

            val result =
                viewModel.addTransaction(
                    categoryId = 4,
                    amount = "0",
                    transactionDate = "2026-11-22T10:00:00Z",
                    comment = null,
                )

            assertFalse(result)
            assertEquals(0, repository.transactions.size)
            assertNotNull(viewModel.error.value)
        }

    @Test
    fun addTransactionRejectsGarbageAmount() =
        runTest {
            val repository = FakeTransactionRepository()
            val viewModel = viewModel(repository)

            val result =
                viewModel.addTransaction(
                    categoryId = 4,
                    amount = "абв",
                    transactionDate = "2026-11-22T10:00:00Z",
                    comment = null,
                )

            assertFalse(result)
            assertEquals(0, repository.transactions.size)
        }

    @Test
    fun deleteTransactionRemovesIt() =
        runTest {
            val repository = FakeTransactionRepository(listOf(transactionModel(id = 3)))
            val viewModel = viewModel(repository, transactionId = 3)

            val result = viewModel.deleteTransaction()

            assertTrue(result)
            assertTrue(repository.transactions.none { it.id == 3 })
        }

    @Test
    fun loadTransactionByIdSetsErrorForMissingTransaction() =
        runTest {
            val viewModel = viewModel(FakeTransactionRepository(), transactionId = 999)

            viewModel.loadTransactionById()

            assertNotNull(viewModel.error.value)
            assertEquals(null, viewModel.transaction.value)
        }
}
