package com.m4isper.myfinances.domain.models

data class AccountModel(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String
)
