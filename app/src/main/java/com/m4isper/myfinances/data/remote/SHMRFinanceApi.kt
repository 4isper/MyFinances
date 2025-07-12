package com.m4isper.myfinances.data.remote

import com.m4isper.myfinances.data.model.AccountDto
import com.m4isper.myfinances.data.model.AccountResponseDto
import com.m4isper.myfinances.data.model.CategoryDto
import com.m4isper.myfinances.data.model.TransactionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface SHMRFinanceApi {
    @GET("categories")
    suspend fun getCategories(): Response<List<CategoryDto>>

    @GET("accounts")
    suspend fun getAccounts(): Response<List<AccountDto>>

    @GET("accounts/{accountId}")
    suspend fun getAccountById(
        @Path("accountId") accountId: Int
    ): Response<AccountDto>

    @PUT("accounts/{accountId}")
    suspend fun updateAccount(
        @Path("accountId") accountId: Int,
        @Body accountDto: AccountDto
    ): Response<AccountResponseDto>

    @GET("transactions/{transactionId}")
    suspend fun getTransactionById(
        @Path("transactionId") transactionId: Int
    ): Response<TransactionDto>

    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactions(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
    ): Response<List<TransactionDto>>
}