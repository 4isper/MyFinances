package com.m4isper.common

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime

class DateUtilsTest {
    @Test
    fun `combineDateTimeToIsoUtc formats date and time as UTC instant`() {
        val instant = DateUtils.combineDateTimeToIsoUtc(LocalDate.of(2026, 11, 22), LocalTime.of(10, 30))

        assertEquals("2026-11-22T10:30:00Z", instant)
    }

    @Test
    fun `extractDate and extractTime restore original values after combine`() {
        val date = LocalDate.of(2026, 1, 5)
        val time = LocalTime.of(23, 45, 10)

        val instant = DateUtils.combineDateTimeToIsoUtc(date, time)

        assertEquals(date, DateUtils.extractDate(instant))
        assertEquals(time, DateUtils.extractTime(instant))
    }

    @Test
    fun `extractDate parses ISO instant with fractional seconds`() {
        val localDate = DateUtils.extractDate("2026-03-01T05:06:07.123456Z")

        assertEquals(LocalDate.of(2026, 3, 1), localDate)
    }

    @Test
    fun `parseDate returns ZonedDateTime in UTC for instant`() {
        val parsed: ZonedDateTime = DateUtils.parseDate("2026-11-22T10:00:00Z")

        assertEquals(0, parsed.offset.totalSeconds)
        assertEquals(LocalDate.of(2026, 11, 22), parsed.toLocalDate())
    }

    @Test
    fun `today and startOfMonth return ISO dates`() {
        val today = DateUtils.today()
        val startOfMonth = DateUtils.startOfMonth()

        assertEquals(today.length, 10)
        assertEquals(startOfMonth.length, 10)
        // формат ISO_DATE: yyyy-MM-dd
        val parts = today.split("-")
        assertEquals(3, parts.size)
        val monthParts = startOfMonth.split("-")
        assertEquals("01", monthParts[2])
    }
}
