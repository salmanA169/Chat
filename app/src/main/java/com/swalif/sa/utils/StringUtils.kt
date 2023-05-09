package com.swalif.sa.utils

import kotlin.random.Random

// TODO: improve function to generate only letter and numbers
fun generateUniqueId():String{
    return Random(6).nextBytes(7).toString()
}