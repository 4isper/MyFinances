package com.m4isper.repository

import com.m4isper.domain.model.TransactionModel
import com.m4isper.local.entity.AccountEntity
import com.m4isper.local.entity.TransactionEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class MapperAndCurrencyTest {
    @Test
    fun accountEntityToModelConvertsCurrencyCodeToSymbol() {
        val model = AccountEntity(id = 1, name = "Основной счёт", balance = "100", currency = "USD").toModel()

        assertEquals("$", model.currency)
        assertEquals("Основной счёт", model.name)
    }

    @Test
    fun accountModelToEntityConvertsSymbolToCurrencyCode() {
        val entity =
            com.m4isper.domain.model
                .AccountModel(id = 1, name = "Основной счёт", balance = "100", currency = "€")
                .toEntity()

        assertEquals("EUR", entity.currency)
    }

    @Test
    fun unknownCurrencyFallsBackToRubAndSymbol() {
        assertEquals("RUB", "GBP".toCurrencyCode())
        assertEquals("₽", "GBP".toCurrencySymbol())
    }

    @Test
    fun currencySymbolsMapRoundTrip() {
        for ((code, symbol) in listOf("RUB" to "₽", "USD" to "$", "EUR" to "€")) {
            assertEquals(symbol, code.toCurrencySymbol())
            assertEquals(code, symbol.toCurrencyCode())
        }
    }

    @Test
    fun transactionEntityModelRoundTrip() {
        val entity =
            TransactionEntity(
                id = 5,
                accountId = 1,
                categoryId = 4,
                amount = "1200",
                comment = "покупка",
                transactionDate = "2026-11-22T10:00:00Z",
                isIncome = false,
                categoryName = "Продукты",
                emoji = "🛒",
                currency = "RUB",
            )

        val model: TransactionModel = entity.toModel()
        assertEquals(5, model.id)
        assertEquals("₽", model.currency)
        assertEquals("покупка", model.comment)

        val backToEntity = model.toEntity()
        assertEquals(entity.copy(currency = "RUB"), backToEntity)
    }
}
