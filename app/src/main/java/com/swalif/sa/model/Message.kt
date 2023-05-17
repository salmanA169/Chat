package com.swalif.sa.model

import logcat.logcat
import java.time.LocalDateTime

data class Message(
    val messageId:Int,
    val chatId:String,
    val senderUid:String,
    val message:String,
    val dateTime:Long,
    val mediaUri:String? = null,
    val statusMessage:MessageStatus,
    val messageType : MessageType
){
    init {

        if (messageType.isMedia() && mediaUri == null){
            error("Media uri must not be null with media type")
        }

    }
    fun isMessageFromMe(uid:String) :Boolean{
        return senderUid == uid
    }
}
enum class MessageType{
    TEXT,IMAGE,AUDIO;

    fun isMedia() = this == IMAGE || this == AUDIO
}
enum class MessageStatus{
    LOADING,SENT,DELIVERED,SEEN;
}
