package com.swalif.sa.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    val chatId:Int = 0,
    val uidSenderUser:String,
    val senderName:String,
    val toUserUid:String,
    val lastMessage:String,
    val imageUri:String,
    val lastMessageDate:LocalDateTime,
    val messagesUnread:Int
)
