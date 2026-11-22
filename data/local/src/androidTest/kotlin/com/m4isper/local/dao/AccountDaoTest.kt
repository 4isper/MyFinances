package com.m4isper.local.dao

import com.m4isper.local.buildTestDatabase
import com.m4isper.local.entity.AccountEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class AccountDaoTest {
    private lateinit var db: com.m4isper.local.AppDatabase
    private lateinit var dao: AccountDao

    @Before
    fun setUp() {
        db = buildTestDatabase()
        dao = db.accountDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    private fun account(
        id: Int = 1,
        currency: String = "RUB",
    ) = AccountEntity(
        id = id,
        name = "Основной счёт",
        balance = "0",
        currency = currency,
    )

    @Test
    fun countReturnsZeroForEmptyTable() =
        runTest {
            assertEquals(0, dao.count())
        }

    @Test
    fun upsertInsertsAndUpdates() =
        runTest {
            dao.upsert(account())

            assertEquals(1, dao.count())
            assertEquals("Основной счёт", dao.getById(1)?.name)

            dao.upsert(account(currency = "USD"))

            assertEquals(1, dao.count())
            assertEquals("USD", dao.getById(1)?.currency)
        }

    @Test
    fun getAllReturnsAllRows() =
        runTest {
            dao.upsert(account(id = 1))
            dao.upsert(account(id = 2))

            assertEquals(2, dao.getAll().size)
        }

    @Test
    fun getByIdReturnsNullForMissingId() =
        runTest {
            assertNull(dao.getById(42))
        }
}
