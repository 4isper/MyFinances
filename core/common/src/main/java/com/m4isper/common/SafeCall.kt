package com.m4isper.common

import kotlin.coroutines.cancellation.CancellationException

/**
 * Оборачивает локальную операцию (Room, KSafe) в Result.
 * Замена safeApiCall после удаления сетевого слоя.
 */
@Suppress("TooGenericExceptionCaught") // осознанно: оборачиваем любой сбой хранилища в Result
suspend fun <T> safeCall(block: suspend () -> T): Result<T> =
    try {
        Result.Success(block())
    } catch (e: CancellationException) {
        // отмену корутины нельзя проглатывать — structured concurrency перестанет работать
        throw e
    } catch (e: Exception) {
        Result.Failure(e)
    }
