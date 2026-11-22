package com.m4isper.local.dao

import com.m4isper.local.buildTestDatabase
import com.m4isper.local.entity.CategoryEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CategoryDaoTest {
    private lateinit var db: com.m4isper.local.AppDatabase
    private lateinit var dao: CategoryDao

    @Before
    fun setUp() {
        db = buildTestDatabase()
        dao = db.categoryDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertAllAndGetAllPersistCategories() =
        runTest {
            val categories =
                listOf(
                    CategoryEntity(id = 1, name = "Зарплата", emoji = "💰", isIncome = true),
                    CategoryEntity(id = 4, name = "Продукты", emoji = "🛒", isIncome = false),
                )

            dao.insertAll(categories)

            val all = dao.getAll()
            assertEquals(2, all.size)
            assertTrue(all.first { it.id == 1 }.isIncome)
            assertFalse(all.first { it.id == 4 }.isIncome)
        }

    @Test
    fun getByIdReturnsCategoryOrNull() =
        runTest {
            dao.insertAll(listOf(CategoryEntity(id = 1, name = "Зарплата", emoji = "💰", isIncome = true)))

            assertEquals("Зарплата", dao.getById(1)?.name)
            assertNull(dao.getById(9))
        }
}
