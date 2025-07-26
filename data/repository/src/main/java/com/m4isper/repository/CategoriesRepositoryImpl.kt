package com.m4isper.repository

import com.m4isper.common.safeApiCall
import com.m4isper.domain.model.CategoryModel
import com.m4isper.domain.repository.CategoriesRepository
import com.m4isper.common.Result
import com.m4isper.remote.SHMRFinanceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
    private val api: SHMRFinanceApi
) : CategoriesRepository {
    override suspend fun getCategories(): Result<List<CategoryModel>> = withContext(Dispatchers.IO) {
        val result = safeApiCall {
            api.getCategories()
        }

        when (result) {
            is Result.Success -> {
                val data = result.data.map { it.toDomain()}
                Result.Success(data)
            }
            is Result.Failure -> result
        }
    }
}