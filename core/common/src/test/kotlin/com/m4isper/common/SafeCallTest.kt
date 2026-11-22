package com.m4isper.common

import com.m4isper.common.error.DomainError
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class SafeCallTest {
    @Test
    fun `returns Success with block result`() =
        runTest {
            val result = safeCall { 42 }

            assertEquals(Result.Success(42), result)
        }

    @Test
    fun `wraps unknown exception into Storage failure`() =
        runTest {
            val cause = IOException("db is gone")

            val result = safeCall { throw cause }

            val error = (result as Result.Failure).exception as DomainError.Storage
            assertEquals(cause, error.cause)
        }

    @Test
    fun `keeps typed domain error as is`() =
        runTest {
            val notFound = DomainError.NotFound("Транзакция", 5)

            val result = safeCall { throw notFound }

            assertEquals(Result.Failure(notFound), result)
        }

    @Test
    fun `rethrows CancellationException instead of wrapping it`() =
        runTest {
            val cancelled = CancellationException("coroutine cancelled")

            try {
                safeCall { throw cancelled }
                assertTrue("Ожидался CancellationException, но safeCall его проглотил", false)
            } catch (e: CancellationException) {
                assertEquals(cancelled, e)
            }
        }
}
