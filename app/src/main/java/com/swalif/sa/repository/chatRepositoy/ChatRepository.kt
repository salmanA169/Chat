package com.swalif.sa.repository.chatRepositoy

import com.swalif.sa.model.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChats(): Flow<List<Chat>>
    suspend fun insertChat(chat: Chat)
    suspend fun deleteChatById(chat:Chat)
    suspend fun readMessages(chatId:Int)
}