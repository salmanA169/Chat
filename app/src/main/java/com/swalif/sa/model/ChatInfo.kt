package com.swalif.sa.model

import java.time.LocalDateTime

data class ChatInfo(
    val userName: String = "",
    val userStatus: UserStatus? = null,
    val uidUser:String = "",
    val imageUri:String = ""
){

}

sealed class UserStatus {
    object Online : UserStatus()
    class Offline (val lastSeen:LocalDateTime): UserStatus()
    object TYPING:UserStatus()

    fun getNextStatus():UserStatus{
        return when(this){
            is Offline -> TYPING
            Online -> Offline(LocalDateTime.now())
            TYPING -> Online
        }
    }
}
