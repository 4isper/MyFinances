package com.m4isper.domain.usecase

import com.m4isper.domain.model.CategoryModel
import com.m4isper.domain.repository.CategoriesRepository
import com.m4isper.common.Result
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository
) {
    suspend operator fun invoke(): Result<List<CategoryModel>> {
        return categoriesRepository.getCategories()
    }
}