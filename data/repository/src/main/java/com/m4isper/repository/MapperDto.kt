package com.m4isper.repository

import com.m4isper.domain.model.AccountModel
import com.m4isper.domain.model.CategoryModel
import com.m4isper.domain.model.TransactionModel
import com.m4isper.domain.model.TransactionResponseModel
import com.m4isper.local.entity.TransactionEntity
import com.m4isper.remote.AccountDto
import com.m4isper.remote.AccountResponseDto
import com.m4isper.remote.CategoryDto
import com.m4isper.remote.TransactionDto
import com.m4isper.remote.TransactionRequestDto
import com.m4isper.remote.TransactionResponseDto

fun TransactionDto.toDomain(): TransactionModel = TransactionModel(
    id = id,
    accountId = account.id,
    isIncome = category.isIncome,
    categoryName = category.name,
    categoryId = category.id,
    amount = amount,
    transactionDate = transactionDate,
    comment = comment,
    emoji = category.emoji,
    currency = account.currency.toCurrencySymbol()
)

fun AccountDto.toDomain(): AccountModel = AccountModel(
    id = id,
    name = name,
    balance = balance,
    currency = currency.toCurrencySymbol()
)

fun CategoryDto.toDomain(): CategoryModel {
    return CategoryModel(
        id = id,
        name = name,
        emoji = emoji,
        isIncome = isIncome
    )
}

fun AccountResponseDto.toDomain(): AccountModel = AccountModel(
    id = id,
    name = name,
    balance = balance,
    currency = currency
)

fun TransactionResponseDto.toDomain(): TransactionResponseModel = TransactionResponseModel(
    id = id,
    accountId = accountId,
    categoryId = categoryId,
    amount = amount,
    transactionDate = transactionDate,
    comment = comment,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun TransactionModel.toDto(): TransactionRequestDto = TransactionRequestDto(
    accountId = accountId,
    categoryId = categoryId,
    amount = amount,
    transactionDate = transactionDate,
    comment = comment
)


fun TransactionModel.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    accountId = accountId,
    categoryId = categoryId,
    categoryName = categoryName,
    amount = amount,
    comment = comment,
    currency = currency,
    transactionDate = transactionDate,
    isIncome = isIncome,
    emoji = emoji,
    isSynced = false,
    updatedAt = System.currentTimeMillis()
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
    currency = currency
)
