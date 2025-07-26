//package com.m4isper.local.dao
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import com.m4isper.local.entity.TransactionEntity
//
//@Dao
//interface TransactionDao {
//    @Query("SELECT * FROM transactions ORDER BY transactionDate DESC")
//    suspend fun getAll(): List<TransactionEntity>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(transaction: TransactionEntity)
//
//    @Query("SELECT * FROM transactions WHERE isSynced = 0")
//    suspend fun getUnsynced(): List<TransactionEntity>
//
//    @Query("DELETE FROM transactions WHERE id = :id")
//    suspend fun deleteById(id: Int)
//
//    @Query("SELECT * FROM transactions WHERE id = :id")
//    suspend fun getById(id: Int): TransactionEntity?
//}
