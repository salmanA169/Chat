package com.swalif.sa.model

import logcat.logcat
import java.time.LocalDateTime

data class Message(
    val messageId:Int,
    val chatId:Int,
    val senderUid:String,
    val message:String,
    val dateTime:LocalDateTime,
    val statusMessage:MessageStatus
){
    fun isMessageFromMe(uid:String) :Boolean{
        return senderUid == uid
    }
}

enum class MessageStatus{
    SENT,DELIVERED,SEEN;

    fun after():MessageStatus{
        return when(this){
            SENT -> DELIVERED
            DELIVERED -> SEEN
            SEEN -> SENT
        }
    }
}
