package com.swalif.sa.repository.firestoreChatMessagesRepo

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.swalif.Constants
import com.swalif.sa.datasource.remote.firestore_dto.ChatDto
import com.swalif.sa.datasource.remote.firestore_dto.MessageDto
import com.swalif.sa.mapper.toListMessageModel
import com.swalif.sa.mapper.toMessageDto
import com.swalif.sa.mapper.toMessageEntity
import com.swalif.sa.mapper.toMessageList
import com.swalif.sa.mapper.toMessageModel
import com.swalif.sa.model.ChatInfo
import com.swalif.sa.model.Message
import com.swalif.sa.repository.chatRepositoy.ChatRepository
import com.swalif.sa.repository.messageRepository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import logcat.logcat
import okhttp3.internal.wait
import javax.inject.Inject

class FirestoreChatWithMessageRepositoryImpl @Inject constructor(
    private val messageRepository: MessageRepository,
    private val chatRepository: ChatRepository,
    private val firestore: FirebaseFirestore
) : FirestoreChatMessageRepository {

    override var isSavedLocally: Boolean = false
        get() = field
        set(value) {
            field = value
        }

    private var isFirstTime = true
    private var chatId = ""
    private var currentCollection :CollectionReference? = null
    val mm = MutableStateFlow<List<Message>>(emptyList())
    override fun getMessage(): Flow<List<Message>> {
//        return messageRepository.getMessages(chatId).map { it.messages.toMessageList() }
        return mm
    }

    override fun addChatId(chatId: String) {
        this.chatId = chatId
    }

    override suspend fun syncMessages() {
           val firestore=  firestore.collection(Constants.CHATS_COLLECTIONS).whereEqualTo("chatId", chatId)
                .get().await()
                .firstOrNull()!!.reference.collection(Constants.MESSAGES_COLLECTIONS)
                currentCollection = firestore
                firestore.snapshots().collect{
                    val messages = it.toObjects<MessageDto>()
                    if (isFirstTime){
                        val messagesDao = getMessage().first().toMutableList()
                            messagesDao.removeAll{message->
                            messages.find { it.chatId == message.chatId }!= null
                        }
                        messagesDao.forEach {
                            messageRepository.addMessage(it)
                        }
                        isFirstTime = false
                    }
                    it.documentChanges.forEach{
                        val message = it.document.toObject<MessageDto>()
                        when(it.type){
                            DocumentChange.Type.ADDED -> {
//                                messageRepository.addMessage(message.toMessageModel())
                                mm.update {
                                    it + message.toMessageModel()
                                }
                                logcat("FirestoreChatWithMessageRepository"){
                                    " added message called"
                                }
                            }
                            DocumentChange.Type.MODIFIED -> {
//                                messageRepository.updateMessage(message.toMessageModel())
                                logcat("FirestoreChatWithMessageRepository"){
                                    " update message called"
                                }
                            }
                            DocumentChange.Type.REMOVED -> {
//                                messageRepository.deleteMessage(message.toMessageModel())
                                logcat("FirestoreChatWithMessageRepository"){
                                    " remove message called"
                                }
                            }
                        }
                    }
                }

    }

    override suspend fun observeMessage() {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(message: MessageDto) {
        currentCollection!!.add(message)
    }

    override suspend fun addChatLocally() {
        TODO("Not yet implemented")
    }

    override suspend fun updateChat() {
        TODO("Not yet implemented")
    }

    override suspend fun getChatInfo(): ChatInfo {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}