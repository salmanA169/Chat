package com.swalif.sa.utils

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun Long.toLocalDateTime(): LocalDateTime = LocalDateTime.ofEpochSecond(this,0, ZoneOffset.UTC)
fun LocalDateTime.toSeconds() = toEpochSecond(ZoneOffset.UTC)

/**
 * // TODO: improve format date for these formatter
 * if his last seen above one day see format like this : last seen **Yesterday -- time--
 * if his last seen equal same day show : -- only time -- 5:12 PM
 * if his last seen above 2 day show only how much day last seen : --day-- 5 days
 */

fun LocalDateTime.formatDateTime(): String = format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))