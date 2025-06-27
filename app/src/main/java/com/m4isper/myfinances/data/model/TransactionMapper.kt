package com.m4isper.myfinances.data.model

import com.m4isper.myfinances.domain.model.AccountModel
import com.m4isper.myfinances.domain.model.CategoryModel
import com.m4isper.myfinances.domain.model.TransactionModel

fun TransactionDto.toDomain(): TransactionModel = TransactionModel(
    id = id,
    isIncome = category.isIncome,
    categoryName = category.name,
    amount = amount,
    transactionDate = transactionDate,
    comment = comment,
    emoji = category.emoji
)

fun AccountDto.toDomain(): AccountModel = AccountModel(
    id = id,
    name = name,
    balance = balance,
    currency = currency
)

fun CategoryDto.toDomain(): CategoryModel {
    return CategoryModel(
        id = id,
        name = name,
        emoji = emoji,
        isIncome = isIncome
    )
}