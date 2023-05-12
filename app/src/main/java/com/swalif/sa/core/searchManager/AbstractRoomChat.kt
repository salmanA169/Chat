package com.swalif.sa.core.searchManager

import com.swalif.sa.model.UserInfo
import kotlinx.coroutines.flow.StateFlow
import java.util.*

abstract class AbstractChatRoom {
    abstract val roomEvent:StateFlow<RoomEvent>
    abstract val maxUsers :Int
    var roomId = UUID.randomUUID().toString()

    abstract fun addUser(userInfo: UserInfo)
    abstract fun removeUser(userInfo: UserInfo)
    abstract fun isBothUsersAccepts():Boolean
    abstract fun updateUserStatus(userUid:String, userState: UserState)
    abstract fun getUIDUsersInRoom():List<String>
}
data class RoomEvent(
    val users :List<Users> = emptyList(),
    val roomStatus: RoomStatus = RoomStatus.WAITING_USERS,
    val startChatRoom :Boolean = false
)
enum class RoomStatus{
    WAITING_USERS,COMPLETE_USERS
}
data class Users(
    val userInfo: UserInfo,
    val userState: UserState
){
    fun isLeft() = userState == UserState.IGNORE
    fun isAccept() = userState == UserState.ACCEPT
}
enum class UserState{
    IDLE,ACCEPT,IGNORE,LEFT
}