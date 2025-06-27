package com.m4isper.myfinances.domain.model

data class CategoryModel(
    val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
)
