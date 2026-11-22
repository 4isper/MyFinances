package com.m4isper.ui.components

import androidx.annotation.StringRes
import com.m4isper.common.error.DomainError
import com.m4isper.resources.R

/** Маппинг доменных ошибок в человекочитаемые ресурсы для UI. */
@StringRes
fun Throwable.toErrorRes(): Int =
    when (this) {
        is DomainError.NotFound -> R.string.error_not_found
        is DomainError.InvalidAmount -> R.string.error_invalid_amount
        else -> R.string.error_storage
    }
