package com.swalif.sa.repository.chatRepositoy

import com.swalif.sa.model.Chat
import com.swalif.sa.model.MessageType
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChats(): Flow<List<Chat>>
    suspend fun insertChat(chat: Chat)
    suspend fun deleteChatById(chat:Chat)
    suspend fun readMessages(chatId:String)
    suspend fun updateChat(chatID: String, text: String, messageType: MessageType)

    suspend fun getChatById(chatId: String):Chat
}