package com.m4isper.domain

import com.m4isper.common.Result
import com.m4isper.domain.model.AccountModel
import com.m4isper.domain.model.CategoryModel
import com.m4isper.domain.model.TransactionModel
import com.m4isper.domain.repository.AccountsRepository
import com.m4isper.domain.repository.CategoriesRepository
import com.m4isper.domain.repository.ThemeRepository
import com.m4isper.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/** Строит TransactionModel с разумными дефолтами — экономит тестам десятки строк. */
fun transactionModel(
    id: Int = 0,
    accountId: Int = 1,
    isIncome: Boolean = false,
    categoryId: Int = 4,
    categoryName: String = "Продукты",
    amount: String = "1000",
    transactionDate: String = "2026-11-22T10:00:00Z",
    currency: String = "₽",
    comment: String? = null,
) = TransactionModel(
    id = id,
    accountId = accountId,
    isIncome = isIncome,
    categoryName = categoryName,
    categoryId = categoryId,
    emoji = "🛒",
    amount = amount,
    transactionDate = transactionDate,
    comment = comment,
    currency = currency,
)

/**
 * Мини-фейки репозиториев для тестов доменных usecases.
 * Дублируют фейки из :core:testing — домен не может зависеть от него.
 */
class FakeTransactionRepository(
    seed: List<TransactionModel> = emptyList(),
) : TransactionRepository {
    val transactions = seed.toMutableList()
    val added = mutableListOf<String>()
    val deletedIds = mutableListOf<Int>()

    override suspend fun getTransactions(
        accountId: Int,
        startDate: String,
        endDate: String,
    ): Result<List<TransactionModel>> = Result.Success(transactions.filter { it.accountId == accountId })

    override suspend fun getTransactionById(transactionId: Int): Result<TransactionModel> {
        val transaction =
            transactions.firstOrNull { it.id == transactionId }
                ?: return Result.Failure(NoSuchElementException("Транзакция $transactionId не найдена"))
        return Result.Success(transaction)
    }

    override suspend fun getIncomeTransactions(
        accountId: Int,
        startDate: String,
        endDate: String,
    ): Result<List<TransactionModel>> = Result.Success(transactions.filter { it.accountId == accountId && it.isIncome })

    override suspend fun getExpenseTransactions(
        accountId: Int,
        startDate: String,
        endDate: String,
    ): Result<List<TransactionModel>> = Result.Success(transactions.filter { it.accountId == accountId && !it.isIncome })

    override suspend fun addTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?,
    ): Result<TransactionModel> {
        added.add(amount)
        val transaction =
            TransactionModel(
                id = transactions.size + 1,
                accountId = accountId,
                isIncome = false,
                categoryName = "Продукты",
                categoryId = categoryId,
                emoji = "🛒",
                amount = amount,
                transactionDate = transactionDate,
                comment = comment,
                currency = "₽",
            )
        transactions.add(transaction)
        return Result.Success(transaction)
    }

    override suspend fun updateTransaction(
        transactionId: Int,
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?,
    ): Result<TransactionModel> {
        val index = transactions.indexOfFirst { it.id == transactionId }
        if (index == -1) {
            return Result.Failure(NoSuchElementException("Транзакция $transactionId не найдена"))
        }
        val updated =
            transactions[index].copy(
                accountId = accountId,
                categoryId = categoryId,
                amount = amount,
                transactionDate = transactionDate,
                comment = comment,
            )
        transactions[index] = updated
        return Result.Success(updated)
    }

    override suspend fun deleteTransaction(transactionId: Int): Result<Unit> {
        deletedIds.add(transactionId)
        transactions.removeAll { it.id == transactionId }
        return Result.Success(Unit)
    }
}

class FakeAccountsRepository(
    seed: List<AccountModel> =
        listOf(
            AccountModel(id = 1, name = "Основной счёт", balance = "0", currency = "₽"),
        ),
) : AccountsRepository {
    val accounts = seed.associateBy { it.id }.toMutableMap()

    override suspend fun getAccounts(): Result<List<AccountModel>> = Result.Success(accounts.values.sortedBy { it.id })

    override suspend fun getAccountById(accountId: Int): Result<AccountModel> {
        val account =
            accounts[accountId]
                ?: return Result.Failure(NoSuchElementException("Счёт $accountId не найден"))
        return Result.Success(account)
    }

    override suspend fun updateAccount(
        accountId: Int,
        name: String,
        balance: String,
        currency: String,
    ): Result<AccountModel> {
        val updated = AccountModel(id = accountId, name = name, balance = balance, currency = currency)
        accounts[accountId] = updated
        return Result.Success(updated)
    }
}

class FakeCategoriesRepository(
    seed: List<CategoryModel> = emptyList(),
) : CategoriesRepository {
    val categories = seed.toMutableList()

    override suspend fun getCategories(): Result<List<CategoryModel>> = Result.Success(categories.toList())
}

class FakeThemeRepository(
    initial: Boolean = false,
) : ThemeRepository {
    private val flow = MutableStateFlow(initial)
    val setCalls = mutableListOf<Boolean>()

    override fun isDarkTheme(): Flow<Boolean> = flow

    override suspend fun setDarkTheme(enabled: Boolean) {
        setCalls.add(enabled)
        flow.value = enabled
    }
}
