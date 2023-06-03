package com.swalif.sa.repository.messageRepository

import com.swalif.sa.datasource.local.entity.MessageEntity
import com.swalif.sa.datasource.local.relation.ChatWithMessages
import com.swalif.sa.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun addMessage(message:Message)
    fun observeMessageByChatId(chatId:String):Flow<ChatWithMessages>
    suspend fun updateMessage(message: Message)
    suspend fun deleteMessages(message: List<Message>)
    suspend fun deleteMessagesByChatId(chatId: String)
    suspend fun deleteMessage(message: Message)
    suspend fun getMessagesByChatID(chatId: String):List<Message>
    suspend fun getAllMessages():List<Message>
}