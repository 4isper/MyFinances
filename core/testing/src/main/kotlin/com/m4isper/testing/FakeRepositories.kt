package com.m4isper.testing

import com.m4isper.common.Result
import com.m4isper.domain.model.AccountModel
import com.m4isper.domain.model.CategoryModel
import com.m4isper.domain.model.TransactionModel
import com.m4isper.domain.repository.AccountsRepository
import com.m4isper.domain.repository.CategoriesRepository
import com.m4isper.domain.repository.CurrencyRepository
import com.m4isper.domain.repository.ThemeRepository
import com.m4isper.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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

class FakeAccountsRepository(
    seedAccounts: List<AccountModel> =
        listOf(
            AccountModel(id = 1, name = "Основной счёт", balance = "0", currency = "₽"),
        ),
) : AccountsRepository {
    val accounts = seedAccounts.associateBy { it.id }.toMutableMap()
    val updatedAccounts = mutableListOf<AccountModel>()

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
        updatedAccounts.add(updated)
        return Result.Success(updated)
    }
}

class FakeTransactionRepository(
    seedTransactions: List<TransactionModel> = emptyList(),
    /** accountId'ы, для которых getTransactions отдаёт Failure — для тестов error-путей. */
    private val failingAccountIds: Set<Int> = emptySet(),
) : TransactionRepository {
    val transactions = seedTransactions.toMutableList()
    private var nextId = (transactions.maxOfOrNull { it.id } ?: 0) + 1

    private fun inPeriod(
        t: TransactionModel,
        startDate: String,
        endDate: String,
    ): Boolean {
        val date = t.transactionDate.substringBefore('T')
        return date >= startDate && date <= endDate
    }

    override suspend fun getTransactions(
        accountId: Int,
        startDate: String,
        endDate: String,
    ): Result<List<TransactionModel>> {
        if (accountId in failingAccountIds) {
            return Result.Failure(RuntimeException("Хранилище недоступно"))
        }
        return Result.Success(
            transactions
                .filter { it.accountId == accountId && inPeriod(it, startDate, endDate) }
                .sortedBy { it.id },
        )
    }

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
    ): Result<List<TransactionModel>> =
        when (val all = getTransactions(accountId, startDate, endDate)) {
            is Result.Success -> Result.Success(all.data.filter { it.isIncome })
            is Result.Failure -> all
        }

    override suspend fun getExpenseTransactions(
        accountId: Int,
        startDate: String,
        endDate: String,
    ): Result<List<TransactionModel>> =
        when (val all = getTransactions(accountId, startDate, endDate)) {
            is Result.Success -> Result.Success(all.data.filter { !it.isIncome })
            is Result.Failure -> all
        }

    override suspend fun addTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?,
    ): Result<TransactionModel> {
        val transaction =
            transactionModel(
                id = nextId++,
                accountId = accountId,
                categoryId = categoryId,
                amount = amount,
                transactionDate = transactionDate,
                comment = comment,
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
        transactions.removeAll { it.id == transactionId }
        return Result.Success(Unit)
    }
}

class FakeCategoriesRepository(
    seedCategories: List<CategoryModel> = emptyList(),
) : CategoriesRepository {
    val categories = seedCategories.toMutableList()

    override suspend fun getCategories(): Result<List<CategoryModel>> = Result.Success(categories.toList())
}

class FakeCurrencyRepository(
    initial: String = "₽",
) : CurrencyRepository {
    private val currencyFlow = MutableStateFlow(initial)
    override val currency: StateFlow<String> = currencyFlow.asStateFlow()

    /** Все значения, переданные в setCurrency, — для проверок синхронизации валюты. */
    val setCurrencyCalls = mutableListOf<String>()

    override fun setCurrency(currencyCode: String) {
        setCurrencyCalls.add(currencyCode)
        currencyFlow.value = currencyCode
    }
}

class FakeThemeRepository(
    initial: Boolean = false,
) : ThemeRepository {
    private val isDarkThemeFlow = MutableStateFlow(initial)
    val setDarkThemeCalls = mutableListOf<Boolean>()

    override fun isDarkTheme(): Flow<Boolean> = isDarkThemeFlow

    override suspend fun setDarkTheme(enabled: Boolean) {
        setDarkThemeCalls.add(enabled)
        isDarkThemeFlow.value = enabled
    }
}
