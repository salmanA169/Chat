package com.swalif.sa.repository.chatRepositoy

import com.swalif.sa.datasource.local.dao.ChatDao
import com.swalif.sa.mapper.toChat
import com.swalif.sa.mapper.toChatEntity
import com.swalif.sa.model.Chat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao
):ChatRepository {
    override fun getChats(): Flow<List<Chat>> {
        return chatDao.getChats().map {
            it.toChat()
        }
    }

    override suspend fun insertChat(chat: Chat) {
        chatDao.insertChat(
            chat.toChatEntity()
        )
    }
}