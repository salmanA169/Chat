package com.swalif.sa.datasource.remote.firestore_dto

data class ChatDto(
    val users:List<UsersChatDto> = listOf(),
    val maxUsers :Int = 0,
    val isAcceptRequestFriends :Boolean = false
)

data class UsersChatDto(
    val userUid:String = "",
    val isLeft:Boolean = false,
    val isTyping:Boolean = false,
    val image:String = "",
    val username:String = ""
)

