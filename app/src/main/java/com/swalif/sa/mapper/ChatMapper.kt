package com.swalif.sa.mapper

import com.swalif.sa.datasource.local.entity.ChatEntity
import com.swalif.sa.datasource.remote.firestore_dto.ChatDto
import com.swalif.sa.model.Chat


fun ChatEntity.toChat() = Chat(
    chatId,
    uidSenderUser,
    toUserUid,
    senderName,
    lastMessage,
    imageUri,
    lastMessageDate,
    messagesUnread,
    lastSenderUid
)

fun Chat.toChatEntity() = ChatEntity(
    chatId,
    uidSenderUser,
    senderName,
    toUserUid,
    lastMessage,
    imageUri,
    lastMessageDate,
    messagesUnread,
    lastSenderUid
)


fun List<ChatEntity>.toChat() = map {
    it.toChat()
}
