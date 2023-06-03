package com.swalif.sa.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class ChatEntity(
    @PrimaryKey(autoGenerate = false)
    val chatId:String = "",
    val uidSenderUser:String,
    val senderName:String,
    val lastMessage:String,
    val imageUri:String,
    val lastMessageDate:Long,
    val messagesUnread:Int,
    val lastSenderUid:String,
    val maxUsers:Int,
    val isSaveLocally :Boolean
)
