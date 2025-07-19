package com.m4isper.myfinances.domain.utils

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateUtils {

    private val formatter = DateTimeFormatter.ISO_DATE

    fun startOfMonth(): String =
        LocalDate.now().withDayOfMonth(1).format(formatter)

    fun today(): String {
        return LocalDate.now().format(formatter)
    }

    fun parseDate(dateString: String): ZonedDateTime =
        ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME)

    fun extractDate(dateString: String): LocalDate =
        parseDate(dateString).toLocalDate()

    fun extractTime(dateString: String): LocalTime {
        val zonedDateTime = ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME)
        return zonedDateTime.toLocalTime()
    }

    fun combineDateTimeToIsoUtc(date: LocalDate, time: LocalTime): String {
        val zonedDateTime = ZonedDateTime.of(date, time, ZoneOffset.UTC)
        return DateTimeFormatter.ISO_INSTANT.format(zonedDateTime.toInstant())
    }
}