package com.swalif.sa.mapper

import com.swalif.sa.datasource.local.entity.MessageEntity
import com.swalif.sa.model.Message

fun MessageEntity.toMessageModel() = Message(
    messageId,chatIdMessage,senderUid,message,dateTime,statusMessage
)

fun Message.toMessageEntity() =  MessageEntity(
    messageId
    ,chatId,senderUid, message, dateTime, statusMessage
)
fun List<MessageEntity>.toMessageList () = map{
    it.toMessageModel()
}