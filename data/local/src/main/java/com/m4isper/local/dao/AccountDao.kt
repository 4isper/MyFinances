package com.m4isper.local.dao

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import com.m4isper.local.entity.AccountEntity

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts")
    suspend fun getAll(): List<AccountEntity>

    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getById(id: Int): AccountEntity?

    @Query("SELECT COUNT(*) FROM accounts")
    suspend fun count(): Int

    @Upsert
    suspend fun upsert(account: AccountEntity)
}
