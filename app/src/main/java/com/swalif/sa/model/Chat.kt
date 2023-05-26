package com.swalif.sa.model

import java.time.LocalDateTime

data class Chat(
    val chatId: String,
    val uidSenderUser: String,
    val toUserUid: String,
    val senderName: String,
    val lastMessage: String,
    val imageUri: String,
    // TODO: create helper class
    val lastMessageDate: Long,
    val messagesUnread: Int,
    val lastSenderUid:String,
)