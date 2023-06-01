package com.swalif.sa.datasource.remote.firestore_dto

import com.swalif.sa.model.RequestFriendStatus

data class ChatDto(
    val users:List<UsersChatDto> = listOf(),
    val maxUsers :Int = 0,
    val acceptRequestFriends :Boolean = false,
    val chatId:String = "",
)

data class UsersChatDto(
    val userUid:String = "",
    val left:Boolean = false,
    val typing:Boolean = false,
    val image:String = "",
    val username:String = "",
    val requestFriend:Boolean = false
)

fun List<UsersChatDto>.formatRequestFriend(myUid:String):RequestFriendStatus{
    if (all { it.requestFriend }){
        return RequestFriendStatus.ACCEPTED
    }
    val getMyCurrentStatus = find { it.userUid == myUid }!!

    return if (getMyCurrentStatus.requestFriend){
        RequestFriendStatus.SENT
    }else{
        RequestFriendStatus.IDLE
    }
}

