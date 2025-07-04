package com.m4isper.myfinances.domain.model

//data class TransactionModel(
//    val id: Int,
//    val account: AccountModel,
//    val category: CategoryModel,
//    val amount: String,
//    val transactionDate: String,
//    val comment: String?,
//    val createdAt: String,
//    val updatedAt: String
//)

data class TransactionModel(
    val id: Int,
    val isIncome: Boolean,
    val categoryName: String,
    val emoji: String,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val currency: String
)
