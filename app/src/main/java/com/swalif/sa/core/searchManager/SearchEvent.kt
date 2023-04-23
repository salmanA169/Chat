package com.swalif.sa.core.searchManager

fun interface SearchEvent {
    fun onEvent(data:RoomEvent)
}
