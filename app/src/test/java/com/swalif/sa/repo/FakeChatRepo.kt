package com.swalif.sa.repo

import com.swalif.sa.model.Chat
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

    override suspend fun readMessages(chatId: Int) {
        TODO("Not yet implemented")
    }
}