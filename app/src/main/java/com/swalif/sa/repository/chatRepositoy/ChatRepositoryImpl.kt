package com.swalif.sa.repository.chatRepositoy

import com.swalif.sa.datasource.local.dao.ChatDao
import com.swalif.sa.mapper.toChat
import com.swalif.sa.mapper.toChatEntity
import com.swalif.sa.model.Chat
import kotlinx.coroutines.coroutineScope
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

    override suspend fun deleteChatById(chat: Chat) {
        chatDao.deleteChatById(chat.toChatEntity())
    }

    override suspend fun readMessages(chatId: Int) {
        val getChat = chatDao.getChatById(chatId)
        chatDao.updateChat(getChat!!.copy(messagesUnread = 0))
    }
}