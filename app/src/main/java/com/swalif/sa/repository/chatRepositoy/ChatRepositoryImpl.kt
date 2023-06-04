package com.swalif.sa.repository.chatRepositoy

import com.google.firebase.Timestamp
import com.swalif.sa.datasource.local.dao.ChatDao
import com.swalif.sa.datasource.local.entity.ChatEntity
import com.swalif.sa.mapper.toChat
import com.swalif.sa.mapper.toChatEntity
import com.swalif.sa.model.Chat
import com.swalif.sa.model.MessageType
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import logcat.logcat
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

    override suspend fun getChatById(chatId: String): Chat? {
        return chatDao.getChatById(chatId)?.toChat()
    }

    override suspend fun insertChat(chat: ChatEntity) {
        chatDao.insertChat(
            chat
        )
    }

    override suspend fun readAllChatMessages(chatId: String) {
       val getChat = chatDao.getChatById(chatId)
        logcat{
            "chat : $getChat"
        }
        getChat?.let {
            chatDao.updateChat(it.copy(messagesUnread = 0))
        }
    }

    override suspend fun deleteChatById(chat: ChatEntity) {
        chatDao.deleteChatById(chat)
    }

    override suspend fun readMessages(chatId: String) {
        val getChat = chatDao.getChatById(chatId)
        chatDao.updateChat(getChat!!.copy(messagesUnread = 0))
    }

    override suspend fun updateChat(chatID: String, text: String, messageType: MessageType,increaseCount:Boolean) {
        val getChat = chatDao.getChatById(chatID)?: return
        val message: String
        when (messageType) {
            MessageType.TEXT -> {
                message = text
            }
            MessageType.IMAGE -> {
                message = "\uD83D\uDDBC️"
            }
            MessageType.AUDIO -> TODO()
            else -> {
                message = text
            }
        }
        chatDao.updateChat(
            getChat.copy(
                lastMessage = message,
                messagesUnread = if (increaseCount)getChat.messagesUnread.plus(1) else 0,
                lastMessageDate = Timestamp.now().toDate().time
            )
        )
    }
}