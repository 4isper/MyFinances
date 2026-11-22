package com.m4isper.common.error

/**
 * Типизированные ошибки домена. UI маппит их в человекочитаемые строки
 * через ресурсы; служебные тексты из data-слоя больше не попадают на экран.
 */
sealed class DomainError(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    class NotFound(
        what: String,
        id: Int,
    ) : DomainError("$what $id не найден")

    class Storage(
        cause: Throwable? = null,
    ) : DomainError("Хранилище недоступно", cause)

    class InvalidAmount(
        val rawAmount: String,
    ) : DomainError("Amount must be greater than zero, was: \"$rawAmount\"")
}
