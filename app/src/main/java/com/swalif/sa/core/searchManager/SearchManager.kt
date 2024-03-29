package com.swalif.sa.core.searchManager

import com.swalif.sa.model.UserInfo
import java.io.Closeable


interface SearchManager :Closeable {
     var onSearchEventListener: SearchEvent?
    fun addSearchEventListener(searchEventListener: SearchEvent) {
        onSearchEventListener = searchEventListener
    }
    suspend fun deleteRoom(roomId:String)
    fun registerSearchEvent(userInfo: UserInfo)
    fun unregisterSearchEvent()
    fun updateUserStatus(userState: UserState)
    fun reload()
}