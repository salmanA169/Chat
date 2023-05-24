package com.swalif.sa.repository.firestoreChatMessagesRepo

import com.swalif.sa.datasource.remote.firestore_dto.MessageDto
import com.swalif.sa.datasource.remote.firestore_dto.UserStatusDto
import com.swalif.sa.model.ChatInfo
import com.swalif.sa.model.Message
import com.swalif.sa.model.UserStatus
import kotlinx.coroutines.flow.Flow
import java.io.Closeable

interface FirestoreChatMessageRepository :Closeable{
    var isSavedLocally :Boolean
    fun getMessage():Flow<List<Message>>
    suspend fun sendMessage(message: MessageDto)
    suspend fun addChatLocally()
    suspend fun updateChat()
    suspend fun getChatInfo():Flow<ChatInfo>
    suspend fun syncMessages()
    suspend fun observeMessage()
    fun addChatId(chatId: String)
    suspend fun updateUserStatus(userStatus: UserStatusDto)
    suspend fun updateUserFriendRequest()
    suspend fun leaveChat()
}