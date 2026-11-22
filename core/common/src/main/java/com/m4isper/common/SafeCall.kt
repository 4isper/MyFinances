package com.m4isper.common

import com.m4isper.common.error.DomainError
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
    } catch (e: DomainError) {
        // типизированные ошибки домена не заворачиваем — их семантика нужна UI
        Result.Failure(e)
    } catch (e: Exception) {
        Result.Failure(DomainError.Storage(e))
    }
