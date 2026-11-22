package com.m4isper.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Upsert
import com.m4isper.local.entity.TransactionEntity

@Dao
interface TransactionDao {
    // transactionDate хранится как ISO_INSTANT ("2026-11-22T02:43:00Z"), границы периода —
    // date-only строки ("2026-11-22"), поэтому обе стороны приводим через date()
    @Query(
        "SELECT * FROM transactions " +
            "WHERE accountId = :accountId " +
            "AND date(transactionDate) BETWEEN date(:startDate) AND date(:endDate)"
    )
    suspend fun getByAccountAndPeriod(
        accountId: Int,
        startDate: String,
        endDate: String
    ): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getById(id: Int): TransactionEntity?

    @Insert
    suspend fun insert(transaction: TransactionEntity): Long

    @Upsert
    suspend fun upsert(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteById(id: Int)
}
