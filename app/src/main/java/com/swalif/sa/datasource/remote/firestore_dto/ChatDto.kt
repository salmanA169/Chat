package com.swalif.sa.datasource.remote.firestore_dto

import androidx.annotation.Keep
import com.swalif.sa.model.RequestFriendStatus
import com.swalif.sa.model.UserInfo

@Keep
data class ChatDto(
    val users:List<UsersChatDto> = listOf(),
    val maxUsers :Int = 0,
    val acceptRequestFriends :Boolean = false,
    val chatId:String = "",
)
@Keep
data class UsersChatDto(
    val userUid:String = "",
    val left:Boolean = false,
    val typing:Boolean = false,
    val image:String = "",
    val username:String = "",
    val requestFriend:Boolean = false
)

fun UserInfo.toUserChat() = UsersChatDto(
    uidUser,image = photoUri, username = userName,
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

