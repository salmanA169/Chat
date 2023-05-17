package com.swalif.sa.datasource.remote.firestore_dto

import com.google.firebase.Timestamp
import com.swalif.sa.model.MessageStatus
import com.swalif.sa.model.MessageType
import java.time.LocalDateTime

data class MessageDto(
    val messageId:Int =0,
    val chatId:String = "",
    val senderUid:String ="",
    val message:String ="",
    val dateTime: Timestamp = Timestamp.now(),
    val mediaUri:String? = null,
    val statusMessage: MessageStatus = MessageStatus.LOADING,
    val messageType : MessageType? = null
)
