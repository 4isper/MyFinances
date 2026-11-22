package com.m4isper.domain

import com.m4isper.common.Result
import com.m4isper.domain.model.CategoryModel
import com.m4isper.domain.usecase.GetCategoriesUseCase
import com.m4isper.domain.usecase.pref.GetDarkThemeUseCase
import com.m4isper.domain.usecase.pref.SetDarkThemeUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CategoriesAndThemeUseCasesTest {
    @Test
    fun `getCategories returns repository list`() =
        runTest {
            val repository =
                FakeCategoriesRepository(
                    listOf(
                        CategoryModel(id = 1, name = "Зарплата", emoji = "💰", isIncome = true),
                        CategoryModel(id = 4, name = "Продукты", emoji = "🛒", isIncome = false),
                    ),
                )

            val result = GetCategoriesUseCase(repository)()

            assertTrue(result is Result.Success)
            assertEquals(2, (result as Result.Success).data.size)
        }

    @Test
    fun `getDarkTheme exposes repository flow value`() =
        runTest {
            val repository = FakeThemeRepository(initial = true)

            val enabled = GetDarkThemeUseCase(repository)().first()

            assertEquals(true, enabled)
        }

    @Test
    fun `setDarkTheme delegates to repository`() =
        runTest {
            val repository = FakeThemeRepository()

            SetDarkThemeUseCase(repository)(true)

            assertEquals(listOf(true), repository.setCalls)
            assertEquals(true, repository.isDarkTheme().first())
        }
}
