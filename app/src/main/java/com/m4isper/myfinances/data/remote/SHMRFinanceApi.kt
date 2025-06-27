package com.m4isper.myfinances.data.remote

import com.m4isper.myfinances.data.model.AccountDto
import com.m4isper.myfinances.data.model.CategoryDto
import com.m4isper.myfinances.data.model.TransactionDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SHMRFinanceApi {
    @GET("categories")
    suspend fun getCategories(): Response<List<CategoryDto>>

    @GET("accounts")
    suspend fun getAccounts(): Response<List<AccountDto>>

    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactions(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
    ): Response<List<TransactionDto>>
}