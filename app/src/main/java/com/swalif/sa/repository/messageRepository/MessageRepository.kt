package com.swalif.sa.repository.messageRepository

import com.swalif.sa.datasource.local.relation.ChatWithMessages
import com.swalif.sa.model.Chat
import com.swalif.sa.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun addMessage(message:Message)
    fun getMessages(chatId:Int):Flow<ChatWithMessages>
    suspend fun updateMessage(message: Message)
    suspend fun deleteMessages(message: List<Message>)
    suspend fun getMessages():List<Message>
}