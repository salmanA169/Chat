package com.swalif.sa.repository.messageRepository

import android.content.Context
import androidx.core.net.toUri
import com.swalif.sa.core.storage.FilesManager
import com.swalif.sa.datasource.local.dao.ChatDao
import com.swalif.sa.datasource.local.dao.MessageDao
import com.swalif.sa.datasource.local.relation.ChatWithMessages
import com.swalif.sa.mapper.toMessageEntity
import com.swalif.sa.model.Message
import com.swalif.sa.model.MessageType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val messageDao: MessageDao,
    private val filesManager: FilesManager,
    private val chatDao: ChatDao
) : MessageRepository {
    override suspend fun addMessage(message: Message) {
        when (message.messageType) {
            MessageType.TEXT -> {
                messageDao.addMessage(message.toMessageEntity())
            }
            MessageType.IMAGE -> {
                val id = messageDao.addMessage(message.copy(mediaUri = "").toMessageEntity())
                val uriImage = message.mediaUri!!
                val nameFile = UUID.randomUUID().toString().plus(".jpeg")
                val rootDir = File(context.filesDir, nameFile).toUri().toString()
                if (filesManager.saveImage(uriImage.toUri(), nameFile)) {
                    messageDao.updateMessage(
                        message.copy(messageId = id.toInt(), mediaUri = rootDir).toMessageEntity()
                    )
                }
            }
            MessageType.AUDIO -> TODO()
        }
        updateChat(message.chatId, message.message, message.messageType)
    }

    private suspend fun updateChat(chatID: Int, text: String, messageType: MessageType) {
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

    override fun getMessages(chatId: Int): Flow<ChatWithMessages> {
        return messageDao.getMessage(chatId)
    }

    override suspend fun updateMessage(message: Message) {
        messageDao.updateMessage(message.toMessageEntity())
    }

    override suspend fun readMessages(chatId: Int) {
        val getChat = chatDao.getChatById(chatId)
        chatDao.updateChat(getChat!!.copy(messagesUnread = 0))
    }
}