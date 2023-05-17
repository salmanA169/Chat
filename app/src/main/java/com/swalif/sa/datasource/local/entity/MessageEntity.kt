package com.swalif.sa.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.swalif.sa.model.MessageStatus
import com.swalif.sa.model.MessageType
import java.time.LocalDateTime
@Entity
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val messageId:Int,
    val chatIdMessage:String,
    val senderUid:String,
    val message:String,
    val dateTime:Long,

    val mediaUri:String?=null,
    val statusMessage: MessageStatus,
    val messageType: MessageType
)
