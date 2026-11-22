package com.m4isper.repository

import com.m4isper.common.Result
import com.m4isper.local.entity.CategoryEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CategoriesRepositoryImplTest {
    private lateinit var categoryDao: FakeCategoryDao
    private lateinit var repository: CategoriesRepositoryImpl

    @Before
    fun setUp() {
        categoryDao = FakeCategoryDao()
        repository = CategoriesRepositoryImpl(categoryDao)
    }

    @Test
    fun getCategoriesSeedsTenDefaultsWhenEmpty() =
        runTest {
            val data = (repository.getCategories() as Result.Success).data

            assertEquals(10, data.size)
            assertEquals((1..10).toList(), data.map { it.id })
            // доходных — 3, расходных — 7
            assertEquals(3, data.count { it.isIncome })
            assertEquals(7, data.count { !it.isIncome })
            assertTrue(data.first { it.id == 1 }.name == "Зарплата")
            assertFalse(data.first { it.id == 4 }.isIncome)
        }

    @Test
    fun seedingIsIdempotent() =
        runTest {
            repository.getCategories()
            repository.getCategories()

            assertEquals(10, categoryDao.rows.size)
        }

    @Test
    fun existingCustomCategoriesAreNotOverwritten() =
        runTest {
            categoryDao.rows.add(CategoryEntity(id = 99, name = "Своя категория", emoji = "⭐", isIncome = false))

            val data = (repository.getCategories() as Result.Success).data

            assertEquals(1, data.size)
            assertEquals("Своя категория", data[0].name)
        }
}
