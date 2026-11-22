package com.m4isper.common

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
    fun `wraps exception into Failure`() =
        runTest {
            val failure = IOException("db is gone")

            val result = safeCall { throw failure }

            assertEquals(Result.Failure(failure), result)
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
