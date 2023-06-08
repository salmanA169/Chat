package com.swalif.sa.repo

import com.swalif.sa.datasource.local.entity.ChatEntity
import com.swalif.sa.model.Chat
import com.swalif.sa.model.MessageType
import com.swalif.sa.repository.chatRepositoy.ChatRepository
import kotlinx.coroutines.flow.*

class FakeChatRepo:ChatRepository {
    private val chats = MutableStateFlow<List<Chat>>(emptyList())
    override fun getChats(): Flow<List<Chat>> {
        return chats.asStateFlow()
    }

    override suspend fun readMessages(chatId: String) {
        TODO("Not yet implemented")
    }



    override suspend fun getChatById(chatId: String): Chat {
        TODO("Not yet implemented")
    }

    override suspend fun insertChat(chat: ChatEntity) {

    }

    override suspend fun deleteChatById(chat: ChatEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun updateChat(
        chatID: String,
        text: String,
        messageType: MessageType,
        increaseCount: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun readAllChatMessages(chatId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteChatById(chatId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun nukeTable() {
        TODO("Not yet implemented")
    }
}