package com.swalif.sa.fakeRepo

import com.swalif.sa.datasource.local.entity.ChatEntity
import com.swalif.sa.model.Chat
import com.swalif.sa.model.MessageType
import com.swalif.sa.repository.chatRepositoy.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ChatFakeRepo @Inject constructor():ChatRepository {
    private val chat = MutableStateFlow<List<Chat>>(listOf())
    override fun getChats(): Flow<List<Chat>> {
        return chat.asStateFlow()
    }

    override suspend fun insertChat(chat: ChatEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteChatById(chat: ChatEntity) {
        TODO("Not yet implemented")
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

    override suspend fun readAllChatMessages(chatId: String) {
        TODO("Not yet implemented")
    }

}