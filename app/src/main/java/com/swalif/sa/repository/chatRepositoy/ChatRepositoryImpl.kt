package com.swalif.sa.repository.chatRepositoy

import com.swalif.sa.datasource.local.dao.ChatDao
import com.swalif.sa.mapper.toChat
import com.swalif.sa.mapper.toChatEntity
import com.swalif.sa.model.Chat
import com.swalif.sa.model.MessageType
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao
):ChatRepository {
    override fun getChats(): Flow<List<Chat>> {
        return chatDao.getChats().map {
            it.toChat()
        }
    }

    override suspend fun getChatById(chatId: Int): Chat {
        return chatDao.getChatById(chatId)!!.toChat()
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

    override suspend fun updateChat(chatID: Int, text: String, messageType: MessageType) {
        val getChat = chatDao.getChatById(chatID)
        val message: String
        when (messageType) {
            MessageType.TEXT -> {
                message = text
            }
            MessageType.IMAGE -> {
                message = "\uD83D\uDDBC️"
            }
            MessageType.AUDIO -> TODO()
        }
        chatDao.updateChat(
            getChat!!.copy(
                lastMessage = message,
                messagesUnread = getChat.messagesUnread.plus(1),
                lastMessageDate = LocalDateTime.now()
            )
        )
    }
}