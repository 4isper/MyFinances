package com.m4isper.myfinances.domain.model

data class AccountModel(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String
)
