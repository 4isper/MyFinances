package com.m4isper.categories

import com.m4isper.domain.model.CategoryModel
import com.m4isper.domain.usecase.GetCategoriesUseCase
import com.m4isper.testing.FakeCategoriesRepository
import com.m4isper.testing.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CategoriesViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val categories =
        listOf(
            CategoryModel(id = 1, name = "Зарплата", emoji = "💰", isIncome = true),
            CategoryModel(id = 4, name = "Продукты", emoji = "🛒", isIncome = false),
            CategoryModel(id = 6, name = "Кафе", emoji = "🍽️", isIncome = false),
        )

    @Test
    fun loadCategoriesFillsState() =
        runTest {
            val viewModel = CategoriesViewModel(GetCategoriesUseCase(FakeCategoriesRepository(categories)))

            viewModel.loadCategories()

            assertEquals(3, viewModel.categories.value.size)
            assertEquals(null, viewModel.error.value)
        }

    @Test
    fun emptyQueryShowsAllCategories() =
        runTest {
            val viewModel = CategoriesViewModel(GetCategoriesUseCase(FakeCategoriesRepository(categories)))
            viewModel.loadCategories()

            viewModel.searchQuery.value = ""

            assertEquals(3, viewModel.filteredCategories.first().size)
        }

    @Test
    fun searchQueryFiltersByNameIgnoringCase() =
        runTest {
            val viewModel = CategoriesViewModel(GetCategoriesUseCase(FakeCategoriesRepository(categories)))
            viewModel.loadCategories()

            viewModel.searchQuery.value = "прод"

            assertEquals(listOf("Продукты"), viewModel.filteredCategories.first().map { it.name })
        }

    @Test
    fun blankQueryBehavesLikeEmpty() =
        runTest {
            val viewModel = CategoriesViewModel(GetCategoriesUseCase(FakeCategoriesRepository(categories)))
            viewModel.loadCategories()

            viewModel.searchQuery.value = "   "

            assertEquals(3, viewModel.filteredCategories.first().size)
        }
}
