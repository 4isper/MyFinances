package com.m4isper.domain.repository

import com.m4isper.domain.model.CategoryModel
import com.m4isper.common.Result

interface CategoriesRepository {
    suspend fun getCategories(): Result<List<CategoryModel>>
}