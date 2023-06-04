package com.swalif.sa.core.searchManager

import com.swalif.sa.datasource.remote.firestore_dto.UserDto
import com.swalif.sa.datasource.remote.firestore_dto.UsersDto
import com.swalif.sa.model.UserInfo
import java.util.UUID

abstract class AbstractChatRoom {
    abstract var users:List<UsersDto>
    abstract var roomStatus:RoomStatus
    abstract val maxUsers :Int
    var roomId = UUID.randomUUID().toString()

    abstract fun addUser(UserInfoDto: UserDto)
    abstract fun removeUser(UserInfoDto: UserDto)
    abstract fun isBothUsersAccepts():Boolean
    abstract fun updateUserStatus(userUid:String, userState: UserState)
    abstract fun getUIDUsersInRoom():List<String>
    abstract fun deleteRoom()
    abstract fun getRoomEvent():RoomEvent
}
data class RoomEvent(
    val users :List<Users> = emptyList(),
    val roomStatus: RoomStatus = RoomStatus.WAITING_USERS,
    val startChatRoom :Boolean = false,
    val roomId:String = ""
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