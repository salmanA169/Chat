package com.swalif.sa.repository.messageRepository

import android.content.Context
import androidx.core.net.toUri
import androidx.room.util.copy
import com.swalif.sa.core.storage.FilesManager
import com.swalif.sa.datasource.local.dao.MessageDao
import com.swalif.sa.datasource.local.entity.MessageEntity
import com.swalif.sa.datasource.local.relation.ChatWithMessages
import com.swalif.sa.mapper.toMessageEntity
import com.swalif.sa.mapper.toMessageList
import com.swalif.sa.mapper.toMessageModel
import com.swalif.sa.model.Message
import com.swalif.sa.model.MessageStatus
import com.swalif.sa.model.MessageType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import logcat.logcat
import java.io.File
import java.util.*
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val messageDao: MessageDao,
    private val filesManager: FilesManager
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

    }

    override fun getMessages(chatId: Int): Flow<ChatWithMessages> {
        return messageDao.getMessage(chatId)
    }

    override suspend fun updateMessage(message: Message) {
        messageDao.updateMessage(message.toMessageEntity())
    }

}