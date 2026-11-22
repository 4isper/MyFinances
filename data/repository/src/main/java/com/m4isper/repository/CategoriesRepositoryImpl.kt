package com.m4isper.repository

import com.m4isper.common.Result
import com.m4isper.common.safeCall
import com.m4isper.domain.model.CategoryModel
import com.m4isper.domain.repository.CategoriesRepository
import com.m4isper.local.dao.CategoryDao
import com.m4isper.local.entity.CategoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoriesRepository {

    override suspend fun getCategories(): Result<List<CategoryModel>> = withContext(Dispatchers.IO) {
        safeCall {
            if (categoryDao.count() == 0) {
                categoryDao.insertAll(DEFAULT_CATEGORIES)
            }
            categoryDao.getAll().map { it.toModel() }
        }
    }

    private fun CategoryEntity.toModel(): CategoryModel = CategoryModel(
        id = id,
        name = name,
        emoji = emoji,
        isIncome = isIncome
    )

    private companion object {
        val DEFAULT_CATEGORIES = listOf(
            CategoryEntity(1, "Зарплата", "💰", isIncome = true),
            CategoryEntity(2, "Подработка", "💼", isIncome = true),
            CategoryEntity(3, "Подарки", "🎁", isIncome = true),
            CategoryEntity(4, "Продукты", "🛒", isIncome = false),
            CategoryEntity(5, "Транспорт", "🚌", isIncome = false),
            CategoryEntity(6, "Кафе", "🍽️", isIncome = false),
            CategoryEntity(7, "Развлечения", "🎬", isIncome = false),
            CategoryEntity(8, "Здоровье", "💊", isIncome = false),
            CategoryEntity(9, "Дом", "🏠", isIncome = false),
            CategoryEntity(10, "Одежда", "👕", isIncome = false)
        )
    }
}
