package com.m4isper.myfinances.data.repository

import com.m4isper.myfinances.data.model.toDomain
import com.m4isper.myfinances.data.remote.SHMRFinanceApi
import com.m4isper.myfinances.data.utils.safeApiCall
import com.m4isper.myfinances.domain.model.CategoryModel
import com.m4isper.myfinances.domain.repository.CategoriesRepository
import com.m4isper.myfinances.domain.utils.Result
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