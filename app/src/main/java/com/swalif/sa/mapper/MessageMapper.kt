package com.swalif.sa.mapper

import com.google.firebase.Timestamp
import com.swalif.sa.datasource.local.entity.MessageEntity
import com.swalif.sa.datasource.remote.firestore_dto.MessageDto
import com.swalif.sa.model.Message
import com.swalif.sa.model.MessageType
import java.util.Date

fun MessageEntity.toMessageModel() = Message(
    messageId, chatIdMessage, senderUid, message, dateTime, mediaUri, statusMessage, messageType
)

fun Message.toMessageEntity() = MessageEntity(
    messageId, chatId, senderUid, message, dateTime, mediaUri, statusMessage, messageType
)
fun Message.toMessageDto() = MessageDto(messageId, chatId, senderUid, message, Timestamp(Date(dateTime)),mediaUri, statusMessage, messageType)

fun MessageDto.toMessageModel() = Message(
    messageId,
    chatId,
    senderUid,
    message,
    dateTime.toDate().time,
    mediaUri,
    statusMessage,
    messageType ?: MessageType.TEXT
)
fun List<MessageDto>.toListMessageModel() = map { it.toMessageModel() }
fun List<MessageEntity>.toMessageList() = map {
    it.toMessageModel()
}