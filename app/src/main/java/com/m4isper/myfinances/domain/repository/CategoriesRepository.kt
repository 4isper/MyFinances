package com.m4isper.myfinances.domain.repository

import com.m4isper.myfinances.domain.model.CategoryModel
import com.m4isper.myfinances.domain.utils.Result

interface CategoriesRepository {
    suspend fun getCategories(): Result<List<CategoryModel>>
}