package com.m4isper.repository

import com.m4isper.local.dao.AccountDao
import com.m4isper.local.dao.CategoryDao
import com.m4isper.local.dao.TransactionDao
import com.m4isper.local.entity.AccountEntity
import com.m4isper.local.entity.CategoryEntity
import com.m4isper.local.entity.TransactionEntity

/**
 * Fake-DAO с семантикой, повторяющей реальные SQL-запросы
 * (в т.ч. сравнение date() по date-only границам периода).
 */
class FakeTransactionDao : TransactionDao {
    val rows = mutableListOf<TransactionEntity>()
    private var nextId = 1

    override suspend fun getByAccountAndPeriod(
        accountId: Int,
        startDate: String,
        endDate: String,
    ): List<TransactionEntity> =
        rows
            .filter { it.accountId == accountId }
            .filter {
                val date = it.transactionDate.substringBefore('T')
                date >= startDate && date <= endDate
            }.sortedBy { it.id }

    override suspend fun getById(id: Int): TransactionEntity? = rows.firstOrNull { it.id == id }

    override suspend fun insert(transaction: TransactionEntity): Long {
        val id = nextId++
        rows.add(transaction.copy(id = id))
        return id.toLong()
    }

    override suspend fun upsert(transaction: TransactionEntity) {
        val index = rows.indexOfFirst { it.id == transaction.id }
        if (index == -1) {
            rows.add(transaction)
        } else {
            rows[index] = transaction
        }
    }

    override suspend fun deleteById(id: Int) {
        rows.removeAll { it.id == id }
    }
}

class FakeAccountDao : AccountDao {
    val rows = mutableListOf<AccountEntity>()

    override suspend fun getAll(): List<AccountEntity> = rows.sortedBy { it.id }

    override suspend fun getById(id: Int): AccountEntity? = rows.firstOrNull { it.id == id }

    override suspend fun count(): Int = rows.size

    override suspend fun upsert(account: AccountEntity) {
        val index = rows.indexOfFirst { it.id == account.id }
        if (index == -1) {
            rows.add(account)
        } else {
            rows[index] = account
        }
    }
}

class FakeCategoryDao : CategoryDao {
    val rows = mutableListOf<CategoryEntity>()

    override suspend fun getAll(): List<CategoryEntity> = rows.sortedBy { it.id }

    override suspend fun getById(id: Int): CategoryEntity? = rows.firstOrNull { it.id == id }

    override suspend fun count(): Int = rows.size

    override suspend fun insertAll(categories: List<CategoryEntity>) {
        rows.addAll(categories)
    }
}
