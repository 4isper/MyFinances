package com.m4isper.repository

import com.m4isper.common.Result
import com.m4isper.local.entity.AccountEntity
import com.m4isper.local.entity.CategoryEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TransactionRepositoryImplTest {
    private lateinit var transactionDao: FakeTransactionDao
    private lateinit var categoryDao: FakeCategoryDao
    private lateinit var accountDao: FakeAccountDao
    private lateinit var repository: TransactionRepositoryImpl

    @Before
    fun setUp() {
        transactionDao = FakeTransactionDao()
        categoryDao = FakeCategoryDao()
        accountDao = FakeAccountDao()
        repository = TransactionRepositoryImpl(transactionDao, categoryDao, accountDao)
    }

    private fun category(
        id: Int = 4,
        name: String = "Продукты",
        emoji: String = "🛒",
        isIncome: Boolean = false,
    ) = CategoryEntity(id = id, name = name, emoji = emoji, isIncome = isIncome)

    private fun account(
        id: Int = 1,
        currency: String = "RUB",
    ) = AccountEntity(
        id = id,
        name = "Основной счёт",
        balance = "0",
        currency = currency,
    )

    private suspend fun insertTransaction(
        id: Int = 0,
        accountId: Int = 1,
        categoryId: Int = 4,
        amount: String = "1000",
        date: String = "2026-11-22T10:00:00Z",
        isIncome: Boolean = false,
    ) {
        val entity =
            com.m4isper.local.entity.TransactionEntity(
                id = id,
                accountId = accountId,
                categoryId = categoryId,
                amount = amount,
                comment = null,
                transactionDate = date,
                isIncome = isIncome,
                categoryName = categoryDao.getById(categoryId)?.name ?: "Продукты",
                emoji = categoryDao.getById(categoryId)?.emoji ?: "🛒",
                currency = "RUB",
            )
        if (id == 0) {
            transactionDao.insert(entity)
        } else {
            transactionDao.rows.add(entity)
        }
    }

    @Test
    fun getTransactionsMapsEntitiesToModels() =
        runTest {
            categoryDao.rows.add(category())
            accountDao.rows.add(account())
            insertTransaction(id = 7, amount = "1500")

            val result = repository.getTransactions(1, "2026-11-01", "2026-11-30")

            val data = (result as Result.Success).data
            assertEquals(1, data.size)
            assertEquals(7, data[0].id)
            assertEquals("1500", data[0].amount)
            assertEquals("₽", data[0].currency)
        }

    @Test
    fun getIncomeAndExpenseTransactionsSplitByIsIncome() =
        runTest {
            categoryDao.rows.add(category())
            categoryDao.rows.add(category(id = 1, name = "Зарплата", emoji = "💰", isIncome = true))
            accountDao.rows.add(account())
            insertTransaction(id = 1, isIncome = false)
            insertTransaction(id = 2, categoryId = 1, isIncome = true, amount = "9000")

            val income = repository.getIncomeTransactions(1, "2026-11-01", "2026-11-30")
            val expense = repository.getExpenseTransactions(1, "2026-11-01", "2026-11-30")

            assertEquals(listOf(2), (income as Result.Success).data.map { it.id })
            assertEquals(listOf(1), (expense as Result.Success).data.map { it.id })
        }

    @Test
    fun addTransactionDerivesFieldsFromCategoryAndAccount() =
        runTest {
            categoryDao.rows.add(category(id = 1, name = "Зарплата", emoji = "💰", isIncome = true))
            accountDao.rows.add(account(currency = "USD"))

            val result =
                repository.addTransaction(
                    accountId = 1,
                    categoryId = 1,
                    amount = "5000",
                    transactionDate = "2026-11-22T10:00:00Z",
                    comment = "аванс",
                )

            val model = (result as Result.Success).data
            assertTrue(model.id > 0)
            assertTrue(model.isIncome)
            assertEquals("Зарплата", model.categoryName)
            assertEquals("💰", model.emoji)
            assertEquals("аванс", model.comment)
            assertEquals("$", model.currency)
            assertEquals("5000", model.amount)
        }

    @Test
    fun addTransactionFallsBackToRubWhenAccountMissing() =
        runTest {
            categoryDao.rows.add(category())

            val result = repository.addTransaction(1, 4, "100", "2026-11-22T10:00:00Z", null)

            assertEquals("₽", (result as Result.Success).data.currency)
        }

    @Test
    fun addTransactionFailsWhenCategoryMissing() =
        runTest {
            val result = repository.addTransaction(1, 999, "100", "2026-11-22T10:00:00Z", null)

            assertTrue(result is Result.Failure)
            assertTrue((result as Result.Failure).exception is NoSuchElementException)
        }

    @Test
    fun updateTransactionMergesWithExistingRow() =
        runTest {
            categoryDao.rows.add(category())
            categoryDao.rows.add(category(id = 1, name = "Зарплата", emoji = "💰", isIncome = true))
            accountDao.rows.add(account())
            insertTransaction(id = 5)

            val result = repository.updateTransaction(5, 1, 1, "777", "2026-11-23T09:00:00Z", "правка")

            val model = (result as Result.Success).data
            assertEquals("777", model.amount)
            assertEquals("Зарплата", model.categoryName)
            assertTrue(model.isIncome)
            assertEquals("правка", model.comment)
        }

    @Test
    fun updateTransactionFailsForMissingTransaction() =
        runTest {
            categoryDao.rows.add(category())

            val result = repository.updateTransaction(42, 1, 4, "1", "2026-11-22T10:00:00Z", null)

            assertTrue(result is Result.Failure)
        }

    @Test
    fun deleteTransactionRemovesRow() =
        runTest {
            categoryDao.rows.add(category())
            accountDao.rows.add(account())
            insertTransaction(id = 3)

            val result = repository.deleteTransaction(3)

            assertEquals(Result.Success(Unit), result)
            assertTrue(transactionDao.rows.none { it.id == 3 })
        }

    @Test
    fun addTransactionRecalculatesAccountBalance() =
        runTest {
            categoryDao.rows.add(category())
            categoryDao.rows.add(category(id = 1, name = "Зарплата", emoji = "💰", isIncome = true))
            accountDao.rows.add(account())

            repository.addTransaction(1, 1, "5 000", "2026-11-22T10:00:00Z", null)
            repository.addTransaction(1, 4, "1 000", "2026-11-22T11:00:00Z", null)

            assertEquals("4 000", accountDao.rows.first { it.id == 1 }.balance)
        }

    @Test
    fun deleteTransactionRestoresBalance() =
        runTest {
            categoryDao.rows.add(category())
            accountDao.rows.add(account())
            val created = repository.addTransaction(1, 4, "1 000", "2026-11-22T10:00:00Z", null)
            val createdId = (created as Result.Success).data.id

            repository.deleteTransaction(createdId)

            assertEquals("0", accountDao.rows.first { it.id == 1 }.balance)
        }

    @Test
    fun updateTransactionRecalculatesBalanceWithTypeChange() =
        runTest {
            categoryDao.rows.add(category())
            categoryDao.rows.add(category(id = 1, name = "Зарплата", emoji = "💰", isIncome = true))
            accountDao.rows.add(account())
            val created = repository.addTransaction(1, 4, "1 000", "2026-11-22T10:00:00Z", null)
            val createdId = (created as Result.Success).data.id

            repository.updateTransaction(createdId, 1, 1, "3 000", "2026-11-22T10:00:00Z", null)

            assertEquals("3 000", accountDao.rows.first { it.id == 1 }.balance)
        }

    @Test
    fun balanceIgnoresTransactionsOfOtherAccounts() =
        runTest {
            categoryDao.rows.add(category())
            accountDao.rows.add(account(id = 1))
            accountDao.rows.add(account(id = 2, currency = "USD"))
            repository.addTransaction(2, 4, "9 000", "2026-11-22T10:00:00Z", null)

            assertEquals("0", accountDao.rows.first { it.id == 1 }.balance)
        }
}
