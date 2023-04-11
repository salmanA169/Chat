package com.swalif.sa.coroutine

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    val main :CoroutineDispatcher
    val io :CoroutineDispatcher
    val default :CoroutineDispatcher
}