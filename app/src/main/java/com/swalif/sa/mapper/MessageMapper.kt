package com.swalif.sa.mapper

import com.swalif.sa.datasource.local.entity.MessageEntity
import com.swalif.sa.model.Message

fun MessageEntity.toMessageModel() = Message(
    messageId, chatIdMessage, senderUid, message, dateTime, mediaUri, statusMessage, messageType
)

fun Message.toMessageEntity() = MessageEntity(
    messageId, chatId, senderUid, message, dateTime, mediaUri, statusMessage, messageType
)

fun List<MessageEntity>.toMessageList() = map {
    it.toMessageModel()
}