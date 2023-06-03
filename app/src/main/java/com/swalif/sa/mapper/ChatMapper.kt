package com.swalif.sa.mapper

import com.swalif.sa.datasource.local.entity.ChatEntity
import com.swalif.sa.datasource.remote.firestore_dto.ChatDto
import com.swalif.sa.model.Chat


fun ChatEntity.toChat() = Chat(
    chatId,
    uidSenderUser,
    senderName,
    lastMessage,
    imageUri,
    lastMessageDate,
    messagesUnread,
    lastSenderUid,
    maxUsers,
    isSaveLocally
)

fun Chat.toChatEntity() = ChatEntity(
    chatId,
    uidSenderUser,
    senderName,
    lastMessage,
    imageUri,
    lastMessageDate,
    messagesUnread,
    lastSenderUid,
    maxUsers,
    isSaveLocally
)


fun List<ChatEntity>.toChat() = map {
    it.toChat()
}
