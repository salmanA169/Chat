package com.swalif.sa.datasource.remote.firestore_dto

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.swalif.sa.model.MessageStatus
import com.swalif.sa.model.MessageType
import java.time.LocalDateTime
import java.util.Date
import kotlin.random.Random

@Keep
data class MessageDto(
    val messageId:Int =0,
    val chatId:String = "",
    val senderUid:String ="",
    val message:String ="",
    val dateTime: Timestamp = Timestamp.now(),
    val mediaUri:String? = null,
    // TODO: test for now
    val statusMessage: MessageStatus = MessageStatus.LOADING,
    val messageType : MessageType? = null
){
    companion object{
        fun createTextMessage(message:String,chatId: String,senderUid: String):MessageDto{
            return MessageDto(
                Random.nextInt(),
                chatId,senderUid,message, messageType = MessageType.TEXT
            )
        }

        fun createImageMessage(image:String,chatId: String,senderUid: String):MessageDto{
            return MessageDto(
                Random.nextInt(),
                chatId,senderUid, mediaUri = image, messageType = MessageType.IMAGE
            )
        }
        fun createAnnouncementMessage(content:String,chatId:String,senderUid: String):MessageDto{
            return MessageDto(
                Random.nextInt(),
                chatId,senderUid,message = content, messageType = MessageType.ANNOUNCEMENT
            )
        }
    }
}

