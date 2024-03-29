package com.swalif.sa.fakeRepo

import com.swalif.sa.datasource.local.dao.MessageDao
import com.swalif.sa.datasource.local.relation.ChatWithMessages
import com.swalif.sa.mapper.toMessageEntity
import com.swalif.sa.model.Message
import com.swalif.sa.repository.messageRepository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FakeMessageRepository @Inject constructor(
    private val messageDao:MessageDao
):MessageRepository {
    override suspend fun addMessage(message: Message) {
        messageDao.addMessage(message.toMessageEntity())
    }

    override suspend fun deleteMessagesByChatId(chatId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun nukeMessageTable() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMessages(message: List<Message>) {
        TODO("Not yet implemented")
    }

    override fun observeMessageByChatId(chatId: String): Flow<ChatWithMessages> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMessage(message: Message) {
        TODO("Not yet implemented")
    }

    override suspend fun getMessagesByChatID(chatId: String): List<Message> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllMessages(): List<Message> {
        TODO("Not yet implemented")
    }

    override suspend fun updateMessage(message: Message) {
        messageDao.updateMessage(message.toMessageEntity())
    }
}