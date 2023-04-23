package com.swalif.sa.repository.searchRepository

import com.swalif.sa.core.searchManager.RoomEvent
import com.swalif.sa.model.UserInfo
import kotlinx.coroutines.flow.Flow
import java.io.Closeable

interface SearchRepository {
    fun searchUser(userInfo:UserInfo): Flow<RoomEvent>
    suspend fun ignoreUser()
    suspend fun acceptUser()
    fun getClosable():Closeable
}
