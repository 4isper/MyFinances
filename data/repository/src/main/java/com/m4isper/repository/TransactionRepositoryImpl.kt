package com.m4isper.repository

import com.m4isper.common.Result
import com.m4isper.common.safeCall
import com.m4isper.domain.model.TransactionModel
import com.m4isper.domain.repository.TransactionRepository
import com.m4isper.domain.utils.formatWithSpaces
import com.m4isper.domain.utils.toCleanDecimal
import com.m4isper.local.dao.AccountDao
import com.m4isper.local.dao.CategoryDao
import com.m4isper.local.dao.TransactionDao
import com.m4isper.local.entity.TransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import javax.inject.Inject

class TransactionRepositoryImpl
    @Inject
    constructor(
        private val transactionDao: TransactionDao,
        private val categoryDao: CategoryDao,
        private val accountDao: AccountDao,
    ) : TransactionRepository {
        override suspend fun getTransactions(
            accountId: Int,
            startDate: String,
            endDate: String,
        ): Result<List<TransactionModel>> =
            withContext(Dispatchers.IO) {
                safeCall {
                    transactionDao
                        .getByAccountAndPeriod(accountId, startDate, endDate)
                        .map { it.toModel() }
                }
            }

        override suspend fun getTransactionById(transactionId: Int): Result<TransactionModel> =
            withContext(Dispatchers.IO) {
                safeCall {
                    transactionDao.getById(transactionId)?.toModel()
                        ?: throw NoSuchElementException("Транзакция $transactionId не найдена")
                }
            }

        override suspend fun getIncomeTransactions(
            accountId: Int,
            startDate: String,
            endDate: String,
        ): Result<List<TransactionModel>> =
            withContext(Dispatchers.IO) {
                when (val result = getTransactions(accountId, startDate, endDate)) {
                    is Result.Success -> Result.Success(result.data.filter { it.isIncome })
                    is Result.Failure -> result
                }
            }

        override suspend fun getExpenseTransactions(
            accountId: Int,
            startDate: String,
            endDate: String,
        ): Result<List<TransactionModel>> =
            withContext(Dispatchers.IO) {
                when (val result = getTransactions(accountId, startDate, endDate)) {
                    is Result.Success -> Result.Success(result.data.filter { !it.isIncome })
                    is Result.Failure -> result
                }
            }

        override suspend fun addTransaction(
            accountId: Int,
            categoryId: Int,
            amount: String,
            transactionDate: String,
            comment: String?,
        ): Result<TransactionModel> =
            withContext(Dispatchers.IO) {
                safeCall {
                    val category =
                        categoryDao.getById(categoryId)
                            ?: throw NoSuchElementException("Категория $categoryId не найдена")
                    val account = accountDao.getById(accountId)

                    val entity =
                        TransactionEntity(
                            id = 0,
                            accountId = accountId,
                            categoryId = categoryId,
                            amount = amount,
                            comment = comment,
                            transactionDate = transactionDate,
                            isIncome = category.isIncome,
                            categoryName = category.name,
                            emoji = category.emoji,
                            currency = account?.currency ?: "RUB",
                        )
                    val newId = transactionDao.insert(entity).toInt()
                    recalculateBalance(accountId)
                    transactionDao.getById(newId)?.toModel()
                        ?: throw IllegalStateException("Транзакция не сохранилась")
                }
            }

        override suspend fun updateTransaction(
            transactionId: Int,
            accountId: Int,
            categoryId: Int,
            amount: String,
            transactionDate: String,
            comment: String?,
        ): Result<TransactionModel> =
            withContext(Dispatchers.IO) {
                safeCall {
                    val existing =
                        transactionDao.getById(transactionId)
                            ?: throw NoSuchElementException("Транзакция $transactionId не найдена")
                    val category =
                        categoryDao.getById(categoryId)
                            ?: throw NoSuchElementException("Категория $categoryId не найдена")

                    val entity =
                        existing.copy(
                            accountId = accountId,
                            categoryId = categoryId,
                            amount = amount,
                            comment = comment,
                            transactionDate = transactionDate,
                            isIncome = category.isIncome,
                            categoryName = category.name,
                            emoji = category.emoji,
                        )
                    transactionDao.upsert(entity)
                    recalculateBalance(accountId)
                    entity.toModel()
                }
            }

        override suspend fun deleteTransaction(transactionId: Int): Result<Unit> =
            withContext(Dispatchers.IO) {
                safeCall {
                    // accountId нужен до удаления, чтобы пересчитать баланс счёта
                    val accountId = transactionDao.getById(transactionId)?.accountId
                    transactionDao.deleteById(transactionId)
                    if (accountId != null) {
                        recalculateBalance(accountId)
                    }
                }
            }

        /**
         * Баланс счёта — производная от всех его транзакций: доходы плюс, расходы минус.
         * Полный пересчёт вместо инкремента — дешевле и устойчивее к расхождениям.
         */
        private suspend fun recalculateBalance(accountId: Int) {
            val account = accountDao.getById(accountId) ?: return
            val total =
                transactionDao
                    .getByAccount(accountId)
                    .fold(BigDecimal.ZERO) { acc, transaction ->
                        val amount = transaction.amount.toCleanDecimal()
                        if (transaction.isIncome) acc + amount else acc - amount
                    }
            accountDao.upsert(account.copy(balance = total.formatWithSpaces()))
        }
    }
