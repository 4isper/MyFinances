package com.m4isper.myfinances.domain.models

data class CategoryModel(
    val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
)
