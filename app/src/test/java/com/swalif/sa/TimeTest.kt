package com.swalif.sa

import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

class TimeTest {

    @Test
    fun testTimes(){
        val date = Date()
        val timeZoned = ZonedDateTime.now(ZoneId.systemDefault())
        println(date)
        println(timeZoned)
    }
}