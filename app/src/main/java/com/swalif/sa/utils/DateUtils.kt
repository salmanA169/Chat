package com.swalif.sa.utils

import java.time.LocalDateTime
import java.time.ZoneOffset

fun Long.toLocalDateTime(): LocalDateTime = LocalDateTime.ofEpochSecond(this,0, ZoneOffset.UTC)
fun LocalDateTime.toSeconds() = toEpochSecond(ZoneOffset.UTC)

