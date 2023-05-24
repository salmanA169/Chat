package com.swalif.sa.utils

import java.util.UUID
import kotlin.random.Random
fun generateId(length: Int= 20): String{
    val alphaNumeric = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return alphaNumeric.shuffled().take(length).joinToString("")  //ex: bwUIoWNCSQvPZh8xaFuz
}
// TODO: improve function to generate only letter and numbers
fun generateUniqueId():String{
    return Random(6).nextBytes(7).toString()
}

fun generateNameFile():String = UUID.randomUUID().toString()