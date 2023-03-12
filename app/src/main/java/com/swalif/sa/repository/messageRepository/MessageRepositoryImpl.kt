package com.swalif.sa.repository.messageRepository

import com.swalif.sa.datasource.local.dao.MessageDao
import com.swalif.sa.datasource.local.entity.MessageEntity
import com.swalif.sa.datasource.local.relation.ChatWithMessages
import com.swalif.sa.mapper.toMessageEntity
import com.swalif.sa.mapper.toMessageList
import com.swalif.sa.mapper.toMessageModel
import com.swalif.sa.model.Message
import com.swalif.sa.model.MessageStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao
) : MessageRepository {
    override suspend fun addMessage(message: Message) {
        messageDao.addMessage(message.toMessageEntity())
    }

    override fun getMessages(chatId: Int): Flow<ChatWithMessages> {
        return messageDao.getMessage(chatId)
    }

    override suspend fun updateMessage(message: Message) {
        messageDao.updateMessage(message.toMessageEntity())
    }

}