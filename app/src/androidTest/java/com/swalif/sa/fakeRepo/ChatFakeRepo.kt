package com.swalif.sa.fakeRepo

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

    override suspend fun deleteChatById(chat: Chat) {
        TODO("Not yet implemented")
    }

    override suspend fun readMessages(chatId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun insertChat(chat: Chat) {
        TODO("Not yet implemented")
    }

    override suspend fun updateChat(chatID: Int, text: String, messageType: MessageType) {
        TODO("Not yet implemented")
    }
}