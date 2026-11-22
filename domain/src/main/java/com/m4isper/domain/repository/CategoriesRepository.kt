package com.m4isper.domain.repository

import com.m4isper.common.Result
import com.m4isper.domain.model.CategoryModel

interface CategoriesRepository {
    suspend fun getCategories(): Result<List<CategoryModel>>
}
