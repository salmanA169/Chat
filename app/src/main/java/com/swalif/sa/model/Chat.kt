package com.swalif.sa.model

import java.time.LocalDateTime

data class Chat(
    val chatId: String,
    val uidSenderUser: String,
    val senderName: String,
    val lastMessage: String,
    val imageUri: String,
    val lastMessageDate: Long,
    val messagesUnread: Int,
    val lastSenderUid:String,
    val maxUsers:Int,
    val isSaveLocally:Boolean = false
)
