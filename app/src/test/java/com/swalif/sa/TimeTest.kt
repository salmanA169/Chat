package com.swalif.sa

import com.google.firebase.firestore.index.OrderedCodeWriter.INFINITY
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round

class TimeTest {

    @Test
    fun testTimes(){
        val y  = 1700.0 /100
        solution(mutableListOf(-23, 4, -3, 8, -12))
    }
}

fun solution(inputArray: MutableList<Int>): Int {
    var x = 0
    var y = 0
    var p = 1
    for( i in 0..inputArray.size){
        x = inputArray[i]
        y = inputArray.getOrNull(i +1)?: break
        if(x*y > p){
            p *= x
        }


    }

    return p
}