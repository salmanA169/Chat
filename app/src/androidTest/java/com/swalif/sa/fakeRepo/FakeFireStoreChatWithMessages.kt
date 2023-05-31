package com.swalif.sa.fakeRepo

import com.swalif.sa.datasource.local.entity.ChatEntity
import com.swalif.sa.datasource.remote.firestore_dto.MessageDto
import com.swalif.sa.datasource.remote.firestore_dto.UserStatusDto
import com.swalif.sa.model.ChatInfo
import com.swalif.sa.model.Message
import com.swalif.sa.repository.firestoreChatMessagesRepo.FirestoreChatMessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FakeFireStoreChatWithMessages @Inject constructor():FirestoreChatMessageRepository {
    override var isSavedLocally: Boolean
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun getMessage(): Flow<List<Message>> {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(message: MessageDto) {
        TODO("Not yet implemented")
    }

    override suspend fun addChatLocally(chat: ChatEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getChatInfo(): Flow<ChatInfo> {
        TODO("Not yet implemented")
    }

    override suspend fun syncMessages() {
        println("fake firestore sync message called")
        TODO("Not yet implemented")
    }

    override suspend fun observeMessage() {
        TODO("Not yet implemented")
    }

    override fun addChatId(chatId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserStatus(userStatus: UserStatusDto) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserFriendRequest() {
        TODO("Not yet implemented")
    }

    override suspend fun leaveChat() {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}