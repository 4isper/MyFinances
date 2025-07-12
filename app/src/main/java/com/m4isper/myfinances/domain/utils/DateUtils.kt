package com.m4isper.myfinances.domain.utils

import java.time.LocalDate
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

    fun extractDate(dateString: String): String {
        val zonedDateTime = parseDate(dateString)
        val timeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return zonedDateTime.format(timeFormatter)
    }

    fun extractTime(dateString: String): String {
        val zonedDateTime = ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME)
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        return zonedDateTime.format(timeFormatter)
    }
}