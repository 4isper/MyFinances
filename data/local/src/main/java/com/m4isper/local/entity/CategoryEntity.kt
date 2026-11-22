package com.m4isper.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean,
)
