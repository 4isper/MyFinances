package com.m4isper.myfinances.data.utils

import kotlinx.coroutines.delay
import retrofit2.Response
import java.io.IOException
import com.m4isper.myfinances.domain.utils.Result

const val MAX_RETRIES = 3
const val RETRY_DELAY_MS = 2000L

suspend fun <T> safeApiCall(
    call: suspend () -> Response<T>
): Result<T> {
    var lastError: Throwable? = null

    repeat(MAX_RETRIES) { attempt ->
        try {
            val response = call()

            if (response.isSuccessful) {
                val body = response.body()
                @Suppress("UNCHECKED_CAST")
                return if (body != null) {
                    Result.Success(body)
                } else {
                    Result.Success(Unit as T)
                }
            }

            if (response.code() in 500..599 && attempt < MAX_RETRIES - 1) {
                delay(RETRY_DELAY_MS)
            } else {
                lastError = Exception("Ошибка ${response.code()}: ${response.message()}")
                return@repeat
            }

        } catch (e: IOException) {
            lastError = e
            return@repeat
        } catch (e: Exception) {
            lastError = e
            return@repeat
        }
    }

    return Result.Failure(lastError ?: Exception("Неизвестная ошибка"))
}
