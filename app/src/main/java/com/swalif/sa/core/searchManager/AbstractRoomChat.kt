package com.swalif.sa.core.searchManager

import com.swalif.sa.model.UserInfo
import kotlinx.coroutines.flow.StateFlow
import java.util.*

abstract class AbstractChatRoom {
    abstract val roomEvent:StateFlow<RoomEvent>
    abstract val maxUsers :Int
    val roomId = UUID.randomUUID().toString()

    abstract fun addUser(userInfo: UserInfo)
    abstract fun removeUser(userInfo: UserInfo)
    abstract fun isBothUsersAccepts():Boolean
    abstract fun updateUserStatus(userUid:String,userStatus: UserStatus)
    abstract fun getUIDUsersInRoom():List<String>
}
data class RoomEvent(
    val users :List<Users> = emptyList(),
    val roomStatus: RoomStatus = RoomStatus.WAITING_USERS
)
enum class RoomStatus{
    WAITING_USERS,COMPLETE_USERS
}
data class Users(
    val userInfo: UserInfo,
    val userStatus: UserStatus
){
    fun isLeft() = userStatus == UserStatus.IGNORE
    fun isAccept() = userStatus == UserStatus.ACCEPT
}
enum class UserStatus{
    IDLE,ACCEPT,IGNORE
}