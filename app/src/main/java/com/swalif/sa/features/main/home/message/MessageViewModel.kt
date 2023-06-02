package com.swalif.sa.features.main.home.message

import androidx.lifecycle.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.swalif.Constants
import com.swalif.sa.CHANNEL_ID_ARG
import com.swalif.sa.IS_SAVED_LOCALLY
import com.swalif.sa.MY_UID_ARG
import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.datasource.remote.firestore_dto.MessageDto
import com.swalif.sa.datasource.remote.firestore_dto.UserStatusDto
import com.swalif.sa.model.*
import com.swalif.sa.repository.firestoreChatMessagesRepo.FirestoreChatMessageRepository
import com.swalif.sa.repository.firestoreChatMessagesRepo.FirestoreChatWithMessageRepositoryImpl
import com.swalif.sa.repository.firestoreChatMessagesRepo.MessageEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import logcat.logcat
import java.io.Closeable
import java.util.Date
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val firestoreChatMessageRepository: FirestoreChatMessageRepository,
    private val dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel(), MessageEvent {
    private val _state = MutableStateFlow(MessageState())
    private val channelID = savedStateHandle.get<String>(CHANNEL_ID_ARG)!!
    private val myUid = savedStateHandle.get<String>(MY_UID_ARG)!!
    private val isSavedLocally = savedStateHandle.get<Boolean>(IS_SAVED_LOCALLY)!!

    private val _leaveChat = MutableLiveData<Boolean>(false)
    val leaveChat :LiveData<Boolean> = _leaveChat
    private val _showDialogLeaveUser = MutableStateFlow(false)
    val showDialogLeaveUser = _showDialogLeaveUser.asStateFlow()


    val state = _state.asStateFlow()

    private var currentJob: Job? = null
    private var isTyping: Boolean = false

    private val _messageEventDataChange = MutableLiveData<Unit?>()
    val messageEventDataChange: LiveData<Unit?> = _messageEventDataChange

    private val _friendEvent = MutableStateFlow<Unit?>(null)
    val friendEvent = _friendEvent.asStateFlow()

    private var messageSize = 0
    override fun onDataChanged() {
        if (messageSize != _state.value.messages.size) {
            _messageEventDataChange.postValue(Unit)
            messageSize = _state.value.messages.size
        }
    }

    override fun onLeaveChat() {
        _leaveChat.postValue(true)
    }

    override fun onFriendAccepted() {
        _friendEvent.value = Unit
    }

    fun restMessageEvent() {
        _messageEventDataChange.value = null
    }

    init {
        if (firestoreChatMessageRepository is FirestoreChatWithMessageRepositoryImpl) {
            firestoreChatMessageRepository.onMessageEventListener = this
        }
        firestoreChatMessageRepository.isSavedLocally = isSavedLocally
        addCloseable(firestoreChatMessageRepository)
        firestoreChatMessageRepository.addChatId(channelID)
        viewModelScope.launch(dispatcherProvider.io) {
            firestoreChatMessageRepository.getMessage().collect { messages ->
                _state.update {
                    it.copy(
                        messages.sortedByDescending { it.dateTime },
                        myUid = myUid
                    )
                }
            }
        }
        viewModelScope.launch(dispatcherProvider.io) {
            firestoreChatMessageRepository.syncMessages()
        }
        viewModelScope.launch(dispatcherProvider.io) {
            firestoreChatMessageRepository.getChatInfo().collect { chatInfo ->
                _state.update {
                    logcat {
                        chatInfo.toString()
                    }
                    it.copy(
                        chatInfo = chatInfo
                    )
                }
            }
        }
    }

    fun updateFriendRequest() {
        viewModelScope.launch(dispatcherProvider.io) {
            firestoreChatMessageRepository.updateUserFriendRequest()
        }
    }

    fun updateTypingUser() {
        if (!isTyping) {
            viewModelScope.launch(dispatcherProvider.io) {
                isTyping = true
                firestoreChatMessageRepository.updateUserStatus(UserStatusDto.TYPING)
                delay(2000)
                isTyping = false
                firestoreChatMessageRepository.updateUserStatus(UserStatusDto.ONLINE)
            }
        }
    }


    fun sendTextMessage(message: String) {
        val message1 = MessageDto.createTextMessage(message, channelID, myUid)
        sendMessage(message1)
//        removeTexts()
    }

    fun sendImage(imageUri: String) {
        val message1 = MessageDto.createImageMessage(imageUri, channelID, myUid)
        sendMessage(message1)
    }

    private fun sendMessage(message: MessageDto) {
        viewModelScope.launch(dispatcherProvider.io) {
            firestoreChatMessageRepository.sendMessage(message)
//            chatRepository.updateChat(message.chatId, message.message, message.messageType)
        }
    }

    private fun removeTexts() {
        _state.update {
            it.copy(
                text = ""
            )
        }
    }

    fun dialogEvent(show: Boolean) {
        _showDialogLeaveUser.update {
            show
        }
    }

    fun leaveChat() {
        viewModelScope.launch(dispatcherProvider.io) {
            firestoreChatMessageRepository.leaveChat()
        }
    }

    fun updateMessage(text: String) {
        _state.update {
            it.copy(
                text = text
            )
        }
    }
}
