package com.swalif.sa.repo

import com.swalif.sa.model.Chat
import com.swalif.sa.model.MessageType
import com.swalif.sa.repository.chatRepositoy.ChatRepository
import kotlinx.coroutines.flow.*

class FakeChatRepo:ChatRepository {
    private val chats = MutableStateFlow<List<Chat>>(emptyList())
    override fun getChats(): Flow<List<Chat>> {
        return chats.asStateFlow()
    }


    override suspend fun insertChat(chat: Chat) {
        chats.emit(chats.value + chat)
    }

    override suspend fun deleteChatById(chat: Chat) {
        chats.value -= chat
    }

    override suspend fun readMessages(chatId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateChat(chatID: String, text: String, messageType: MessageType) {
        TODO("Not yet implemented")
    }

    override suspend fun getChatById(chatId: String): Chat {
        TODO("Not yet implemented")
    }
}