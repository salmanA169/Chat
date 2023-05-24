package com.swalif.sa.repo

import com.swalif.sa.datasource.local.relation.ChatWithMessages
import com.swalif.sa.model.Message
import com.swalif.sa.repository.messageRepository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MessageFakeRepository:MessageRepository {
    private val messages = MutableStateFlow<List<Message>>(listOf())
    override suspend fun addMessage(message: Message) {
        TODO("Not yet implemented")
    }

    override fun getMessages(chatId: String): Flow<ChatWithMessages> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMessage(message: Message) {
        TODO("Not yet implemented")
    }

    override suspend fun updateMessage(message: Message) {

    }


    override suspend fun deleteMessages(message: List<Message>) {
//        messages.removeAll(message)
    }

    override suspend fun getMessages(): List<Message> {
        TODO()
    }

}