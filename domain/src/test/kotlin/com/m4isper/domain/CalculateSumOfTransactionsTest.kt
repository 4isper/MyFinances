package com.m4isper.domain

import com.m4isper.domain.model.TransactionModel
import com.m4isper.domain.utils.calculateSumOfTransactions
import com.m4isper.domain.utils.formatWithSpaces
import com.m4isper.domain.utils.toCleanDecimal
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class CalculateSumOfTransactionsTest {
    private fun transaction(amount: String) =
        TransactionModel(
            id = 0,
            accountId = 1,
            isIncome = false,
            categoryName = "Продукты",
            categoryId = 4,
            emoji = "🛒",
            amount = amount,
            transactionDate = "2026-11-22T10:00:00Z",
            comment = null,
            currency = "₽",
        )

    @Test
    fun `sums integer amounts`() {
        val sum =
            calculateSumOfTransactions(
                listOf(transaction("1000"), transaction("250"), transaction("50")),
            )

        assertEquals("1 300", sum)
    }

    @Test
    fun `sums amounts with inner spaces`() {
        val sum =
            calculateSumOfTransactions(
                listOf(transaction("5 000"), transaction("1 500")),
            )

        assertEquals("6 500", sum)
    }

    @Test
    fun `empty list sums to zero`() {
        val sum = calculateSumOfTransactions(emptyList())

        assertEquals("0", sum)
    }

    @Test
    fun `invalid amount counts as zero`() {
        val sum =
            calculateSumOfTransactions(
                listOf(transaction("abc"), transaction("300")),
            )

        assertEquals("300", sum)
    }

    @Test
    fun `null amount cleaned to zero`() {
        assertEquals(BigDecimal.ZERO, null.toCleanDecimal())
    }

    @Test
    fun `whitespace is stripped before parsing`() {
        assertEquals(BigDecimal("12345"), "12 345".toCleanDecimal())
    }

    @Test
    fun `clean decimal of garbage is zero`() {
        assertEquals(BigDecimal.ZERO, "не число".toCleanDecimal())
    }

    @Test
    fun `format with spaces groups thousands`() {
        assertEquals("12 345", BigDecimal("12345").formatWithSpaces())
        assertEquals("999", BigDecimal("999").formatWithSpaces())
    }
}
