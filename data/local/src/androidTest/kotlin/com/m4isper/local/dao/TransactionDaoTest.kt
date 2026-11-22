package com.m4isper.local.dao

import com.m4isper.local.buildTestDatabase
import com.m4isper.local.entity.TransactionEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TransactionDaoTest {
    private lateinit var db: com.m4isper.local.AppDatabase
    private lateinit var dao: TransactionDao

    @Before
    fun setUp() {
        db = buildTestDatabase()
        dao = db.transactionDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    private fun transaction(
        id: Int = 0,
        accountId: Int = 1,
        amount: String = "1000",
        date: String = "2026-11-22T10:00:00Z",
    ) = TransactionEntity(
        id = id,
        accountId = accountId,
        categoryId = 4,
        amount = amount,
        comment = null,
        transactionDate = date,
        isIncome = false,
        categoryName = "Продукты",
        emoji = "🛒",
        currency = "RUB",
    )

    /** Регрессия: раньше transactionDate сравнивали строкой с date-only границами и теряли всё. */
    @Test
    fun getByAccountAndPeriodFindsIsoInstantTimestampWithDateOnlyBounds() =
        runTest {
            dao.insert(transaction(date = "2026-11-22T10:00:00Z"))

            val found = dao.getByAccountAndPeriod(1, "2026-11-22", "2026-11-22")

            assertEquals(1, found.size)
            assertEquals("2026-11-22T10:00:00Z", found.first().transactionDate)
        }

    @Test
    fun getByAccountAndPeriodRespectsPeriodBounds() =
        runTest {
            dao.insert(transaction(date = "2026-11-21T23:00:00Z"))
            dao.insert(transaction(date = "2026-11-22T00:00:00Z"))
            dao.insert(transaction(date = "2026-11-23T23:59:59Z"))

            val found = dao.getByAccountAndPeriod(1, "2026-11-22", "2026-11-22")

            assertEquals(listOf("2026-11-22T00:00:00Z"), found.map { it.transactionDate })
        }

    @Test
    fun getByAccountAndPeriodFiltersByAccountId() =
        runTest {
            dao.insert(transaction(accountId = 1))
            dao.insert(transaction(accountId = 2, date = "2026-11-22T11:00:00Z"))

            val found = dao.getByAccountAndPeriod(1, "2026-11-22", "2026-11-22")

            assertEquals(1, found.size)
            assertEquals(1, found.first().accountId)
        }

    @Test
    fun getByAccountReturnsOnlyAccountRows() =
        runTest {
            dao.insert(transaction(accountId = 1))
            dao.insert(transaction(accountId = 2, date = "2026-11-23T10:00:00Z"))

            val rows = dao.getByAccount(1)

            assertEquals(1, rows.size)
            assertEquals(1, rows.first().accountId)
        }

    @Test
    fun insertGeneratesIdAndGetByIdReturnsSavedRow() =
        runTest {
            val id = dao.insert(transaction()).toInt()

            val loaded = dao.getById(id)

            assertTrue(id > 0)
            assertEquals("1000", loaded?.amount)
            assertEquals("Продукты", loaded?.categoryName)
        }

    @Test
    fun getByIdReturnsNullForMissingRow() =
        runTest {
            assertNull(dao.getById(999))
        }

    @Test
    fun upsertUpdatesExistingRow() =
        runTest {
            val id = dao.insert(transaction()).toInt()

            dao.upsert(transaction(id = id, amount = "5000"))

            assertEquals("5000", dao.getById(id)?.amount)
        }

    @Test
    fun deleteByIdRemovesRow() =
        runTest {
            val id = dao.insert(transaction()).toInt()

            dao.deleteById(id)

            assertNull(dao.getById(id))
        }
}
