package com.m4isper.myfinances.domain.usecase

import com.m4isper.myfinances.data.repository.CategoriesRepository
import com.m4isper.myfinances.domain.model.CategoryModel
import com.m4isper.myfinances.domain.utils.Result
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository
) {
    suspend operator fun invoke(): Result<List<CategoryModel>> {
        return categoriesRepository.getCategories()
    }
}