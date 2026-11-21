package com.m4isper.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val currency: String
)