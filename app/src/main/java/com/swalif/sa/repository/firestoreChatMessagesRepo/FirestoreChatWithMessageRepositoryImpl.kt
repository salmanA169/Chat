package com.swalif.sa.repository.firestoreChatMessagesRepo

import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.swalif.Constants
import com.swalif.sa.core.storage.FilesManager
import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.datasource.local.entity.ChatEntity
import com.swalif.sa.datasource.remote.firestore_dto.ChatDto
import com.swalif.sa.datasource.remote.firestore_dto.MessageDto
import com.swalif.sa.datasource.remote.firestore_dto.UserDto
import com.swalif.sa.datasource.remote.firestore_dto.UserStatusDto
import com.swalif.sa.datasource.remote.firestore_dto.formatRequestFriend
import com.swalif.sa.datasource.remote.firestore_dto.localizeToUserStatus
import com.swalif.sa.mapper.toListMessageModel
import com.swalif.sa.mapper.toMessageList
import com.swalif.sa.mapper.toMessageModel
import com.swalif.sa.model.ChatInfo
import com.swalif.sa.model.Message
import com.swalif.sa.model.MessageStatus
import com.swalif.sa.model.MessageType
import com.swalif.sa.model.SenderInfo
import com.swalif.sa.repository.chatRepositoy.ChatRepository
import com.swalif.sa.repository.messageRepository.MessageRepository
import com.swalif.sa.repository.userRepository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import logcat.logcat
import javax.inject.Inject

// TODO: fix announcement issue
class FirestoreChatWithMessageRepositoryImpl @Inject constructor(
    private val messageRepository: MessageRepository,
    private val chatRepository: ChatRepository,
    private val firestore: FirebaseFirestore,
    private val userRepository: UserRepository,
    private val fireStorage: FirebaseStorage,
    private val filesManager: FilesManager,
    private val dispatcherProvider: DispatcherProvider
) : FirestoreChatMessageRepository {

    override var isSavedLocally: Boolean = false
        get() = field
        set(value) {
            // TODO: here update chat when is saved
            field = value

        }

    private val job = Job()
    private val coroutineScope = CoroutineScope(dispatcherProvider.io + job)
    private var isFirstTime = true
    private var chatId = ""
    private var currentMessagesCollection: CollectionReference? = null
    private var currentChatDocument: DocumentReference? = null
    val mm = MutableStateFlow<List<Message>>(emptyList())
    private var currentChatDto: ChatDto? = null
    private var myUid: String? = null
    var onMessageEventListener: MessageEvent? = null
        set(value) {
            field = value
        }


    override suspend fun updateUserStatus(userStatus: UserStatusDto) {
        // TODO: change to document equal uid user
        firestore.collection(Constants.USERS_COLLECTIONS).whereEqualTo("uidUser", myUid!!).get()
            .await().first().apply {
                reference.update("userStatus", userStatus).await()
            }

    }

    override fun getMessage(): Flow<List<Message>> {
//        return messageRepository.getMessages(chatId).map { it.messages.toMessageList() }
        return mm
    }

    override fun addChatId(chatId: String) {
        this.chatId = chatId
    }

    override suspend fun syncMessages() {
        readAllMessageLocally(chatId)
        if (isSavedLocally) {
            observeMessage()
        }
        val firestore =
            firestore.collection(Constants.CHATS_COLLECTIONS).whereEqualTo("chatId", chatId)
                .get().await()
                .firstOrNull()!!.reference.apply { currentChatDocument = this }
                .collection(Constants.MESSAGES_COLLECTIONS)
        currentMessagesCollection = firestore
        // TODO: fix it when app in background still observe.. use LocalViewModelStoreOwner in message screen to clear
        firestore.snapshots().collect {
            logcat("FirestoreChatMessageRepository sync message") {
                "is save locally : $isSavedLocally"
            }
            if (isFirstTime && isSavedLocally) {
                val messages = it.toObjects<MessageDto>()
                coroutineScope.launch {
                    syncMessageLocally(messages.toListMessageModel())
                }
            }
            it.documents.filter {
                val message = it.toObject(MessageDto::class.java)
                message?.statusMessage == MessageStatus.SENT && message.senderUid != myUid
            }.map {
                it.reference
            }.forEach {
                coroutineScope.launch {
                    readMessages(it)
                }
            }
            if (!isFirstTime || !isSavedLocally) {
                it.documentChanges.forEach {
                    logcat("FirestoreChatWithMessages"){
                        "${it.type}"
                    }
                    val message = it.document.toObject<MessageDto>()
                    when (it.type) {
                        DocumentChange.Type.ADDED -> {
//                                messageRepository.addMessage(message.toMessageModel())
//                        Log.d("FirestoreChatWithMessage",message.toString())
                            val list = mm.value.toMutableList()
                            if (list.find { it.messageId == message.messageId } != null) {
                                val getIndex =
                                    list.indexOfFirst { it.messageId == message.messageId }
                                list[getIndex] = message.toMessageModel()
                            } else {
                                list.add(message.toMessageModel())
                            }
                            if (isSavedLocally) {
                                messageRepository.addMessage(message.toMessageModel())
//                                chatRepository.updateChat(
//                                    chatId,
//                                    message.message,
//                                    message.messageType!!
//                                )
                            } else {
                                mm.update {
                                    list
                                }
                            }
                        }

                        DocumentChange.Type.MODIFIED -> {
                            val list = mm.value.toMutableList()
                            val index = list.indexOfFirst { it.messageId == message.messageId }
                            list[index] = message.toMessageModel()
                            if (isSavedLocally) {
                                messageRepository.updateMessage(message.toMessageModel())
                            } else {
                                mm.update {
                                    list
                                }
                            }
                        }

                        DocumentChange.Type.REMOVED -> {
//                                messageRepository.deleteMessage(message.toMessageModel())
                            logcat("FirestoreChatWithMessageRepository") {
                                " remove message called"
                            }
                        }
                    }
                }
            }

            onMessageEventListener?.let {
                it.onDataChanged()
            }
            isFirstTime = false
        }

    }

    private suspend fun readMessages(documentReference: DocumentReference) {
        documentReference.update("statusMessage", MessageStatus.SEEN).await()
        readAllMessageLocally(chatId)
    }

    private suspend fun readAllMessageLocally(chatId: String){
        if (isSavedLocally){
            chatRepository.readAllChatMessages(chatId)
        }
    }

    override suspend fun observeMessage() {
        coroutineScope.launch {
            messageRepository.observeMessageByChatId(chatId).collect { messages ->
                mm.update {
                    messages.messages.toMessageList()
                }
            }
        }
    }

    override suspend fun sendMessage(message: MessageDto) {

        when (message.messageType) {
            MessageType.TEXT -> {
                mm.update {
                    it + message.toMessageModel()
                }
                currentMessagesCollection!!.add(message.copy(statusMessage = MessageStatus.SENT))
            }

            MessageType.IMAGE -> {
                mm.update {
                    it + message.toMessageModel().copy(mediaUri = "")
                }
                val image = saveToFireStorage(myUid!!, message.mediaUri!!.toUri())
                val messageUpdated =
                    message.copy(mediaUri = image, statusMessage = MessageStatus.SENT)
                currentMessagesCollection!!.add(messageUpdated)
            }

            MessageType.AUDIO -> TODO()
            MessageType.ANNOUNCEMENT -> {

            }
            null -> TODO()
        }

    }


    private suspend fun saveToFireStorage(pathUidUser: String, mediaUri: Uri): String? {
        return try {
            val compressImage = filesManager.compressImage(mediaUri)
            val reference = fireStorage.reference.child(pathUidUser)
                .child(mediaUri.lastPathSegment!!.plus(".jpeg")).putBytes(compressImage).await()
            reference.storage.downloadUrl.await().toString()
        } catch (f: Exception) {
            logcat("FirestoreChatMessageRepository") {
                "save file faield : ${f.message}"
            }
            null
        }

    }

    override suspend fun addChatLocally(chat: ChatEntity) {
        chatRepository.insertChat(chat)
    }

    private suspend fun checkAnyNewUpdateChatLocally(
        chatId: String,
        message: String,
        type: MessageType
    ) {
        chatRepository.updateChat(chatId, message, type)
    }

    override suspend fun getChatInfo(): Flow<ChatInfo> {
        val getMyUser = userRepository.getCurrentUser()!!
        myUid = getMyUser.uidUser
        val getReceiver =
            firestore.collection(Constants.CHATS_COLLECTIONS).whereEqualTo("chatId", chatId)
                .snapshots().map {
                    val chat = it.first()
                    val usersChat = chat.toObject(ChatDto::class.java)
                    currentChatDto = usersChat
                    val findReceiver = usersChat.users.find { it.userUid != getMyUser.uidUser }!!
                    if (!isSavedLocally) {
                        if (usersChat.acceptRequestFriends) {
                            onMessageEventListener?.onFriendAccepted()
                            isSavedLocally = true
                            saveUserLocally(
                                usersChat.chatId,
                                SenderInfo(
                                    findReceiver.userUid,
                                    findReceiver.image,
                                    findReceiver.username
                                ), usersChat.maxUsers
                            )
                        }
                    }
                    findReceiver
                }
        val receiverUid = getReceiver.first().userUid
        val receiveStatus =
            firestore.collection(Constants.USERS_COLLECTIONS).whereEqualTo("uidUser", receiverUid)
                .dataObjects<UserDto>().map { it.first() }
        return combine(getReceiver, receiveStatus) { receiverInfo, receiverStatus ->
            logcat("FirestoreChatWithMessage:getChat Info") {
                "called ${receiverStatus.userStatus}"
            }

            ChatInfo(
                receiverInfo.username,
                receiverStatus.localizeToUserStatus(),
                receiverInfo.userUid,
                receiverInfo.image,
                receiverInfo.isLeft,
                currentChatDto!!.users.formatRequestFriend(myUid!!)
            )
        }
    }

    private suspend fun saveUserLocally(chatId: String, senderInfo: SenderInfo, maxUsers: Int) {
        val message = mm.value.last()
        val chatEntity = ChatEntity(
            chatId,
            senderInfo.uid,
            senderInfo.username,
            message.message, senderInfo.image, message.dateTime, 0, message.senderUid, maxUsers
        )
        addChatLocally(chatEntity)
        syncMessageLocally(mm.value)
        observeMessage()
    }

    private suspend fun syncMessageLocally(messages: List<Message>) {
        val getMessageLocal = messageRepository.getMessagesByChatID(chatId)
        val messages = messages.toMutableList()

        messages.removeAll{list1->
            getMessageLocal.any{
                list1.messageId == it.messageId
            }
        }
        messages.forEach {
            logcat("FirestoreChatWithMessages"){
                "called ${it.messageId}"
            }
            coroutineScope.launch(dispatcherProvider.io) {
                messageRepository.addMessage(it)
            }
        }
        messages.lastOrNull { it.messageType != MessageType.ANNOUNCEMENT }?.let {
            checkAnyNewUpdateChatLocally(chatId, it.message, it.messageType)
        }
    }

    override suspend fun updateUserFriendRequest() {
        val getCurrentUser = currentChatDto!!.users.find { it.userUid == myUid }!!
        val updatedCurrentUser = getCurrentUser.copy(requestFriend = true)
        val newList = currentChatDto!!.users.toMutableList()
        val index = newList.indexOfFirst { it.userUid == myUid }
        newList[index] = updatedCurrentUser
        val isAcceptedAsFriend = newList.all { it.requestFriend }
        val newData =
            currentChatDto!!.copy(users = newList, acceptRequestFriends = isAcceptedAsFriend)
        currentChatDocument!!.apply {
            set(newData).await()
            val getReceiverUser = newData.users.find { it.userUid != myUid }?: return
            if (!getReceiverUser.requestFriend){
                val findMyUser = newData.users.find { it.userUid == myUid }?: return
                currentMessagesCollection?.add(
                    MessageDto.createAnnouncementMessage(
                        "${findMyUser.username} sent request friend",chatId,findMyUser.userUid
                    )
                )?.await()
            }
        }

    }

    override suspend fun leaveChat() {
        TODO("Not yet implemented")
    }

    override fun close() {
        job.cancel()
        currentMessagesCollection = null
        currentChatDto = null
        currentChatDocument = null
    }
}