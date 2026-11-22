package com.m4isper.repository

import com.m4isper.domain.model.AccountModel
import com.m4isper.domain.model.TransactionModel
import com.m4isper.local.entity.AccountEntity
import com.m4isper.local.entity.TransactionEntity

fun AccountEntity.toModel(): AccountModel = AccountModel(
    id = id,
    name = name,
    balance = balance,
    currency = currency.toCurrencySymbol()
)

fun AccountModel.toEntity(): AccountEntity = AccountEntity(
    id = id,
    name = name,
    balance = balance,
    currency = currency.toCurrencyCode()
)

fun TransactionEntity.toModel(): TransactionModel = TransactionModel(
    id = id,
    accountId = accountId,
    isIncome = isIncome,
    categoryName = categoryName,
    categoryId = categoryId,
    emoji = emoji,
    amount = amount,
    transactionDate = transactionDate,
    comment = comment,
    currency = currency.toCurrencySymbol()
)

fun TransactionModel.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    accountId = accountId,
    categoryId = categoryId,
    categoryName = categoryName,
    amount = amount,
    comment = comment,
    currency = currency.toCurrencyCode(),
    transactionDate = transactionDate,
    isIncome = isIncome,
    emoji = emoji
)
