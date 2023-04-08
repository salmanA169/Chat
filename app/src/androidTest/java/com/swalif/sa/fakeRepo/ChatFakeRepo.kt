package com.swalif.sa.fakeRepo

import com.swalif.sa.model.Chat
import com.swalif.sa.repository.chatRepositoy.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatFakeRepo @Inject constructor():ChatRepository {
    override fun getChats(): Flow<List<Chat>> {
        TODO("Not yet implemented")
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
}