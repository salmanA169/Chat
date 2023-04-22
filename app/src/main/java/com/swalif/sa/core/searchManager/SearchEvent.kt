package com.swalif.sa.core.searchManager

fun interface SearchEvent<T> {
    fun onEvent(data:T)
}