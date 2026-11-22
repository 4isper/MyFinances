package com.m4isper.common

/**
 * Оборачивает локальную операцию (Room, KSafe) в Result.
 * Замена safeApiCall после удаления сетевого слоя.
 */
suspend fun <T> safeCall(block: suspend () -> T): Result<T> = try {
    Result.Success(block())
} catch (e: Exception) {
    Result.Failure(e)
}
