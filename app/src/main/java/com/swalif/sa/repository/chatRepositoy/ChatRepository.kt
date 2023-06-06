package com.swalif.sa.repository.chatRepositoy

import com.swalif.sa.datasource.local.entity.ChatEntity
import com.swalif.sa.model.Chat
import com.swalif.sa.model.MessageType
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChats(): Flow<List<Chat>>
    suspend fun insertChat(chat: ChatEntity)
    suspend fun deleteChatById(chat:ChatEntity)
    suspend fun readMessages(chatId:String)
    suspend fun updateChat(chatID: String, text: String, messageType: MessageType,increaseCount:Boolean = false)

    suspend fun getChatById(chatId: String):Chat?

    suspend fun readAllChatMessages(chatId:String)

    suspend fun nukeTable()
}