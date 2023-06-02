package com.swalif.sa.utils

import com.swalif.sa.Screens
import java.util.UUID
import kotlin.random.Random
fun generateUniqueId(length: Int= 7): String{
    val alphaNumeric = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return alphaNumeric.shuffled().take(length).joinToString("")  //ex: bwUIoWNCSQvPZh8xaFuz
}
// TODO: improve function to generate only letter and numbers


fun generateNameFile():String = UUID.randomUUID().toString()