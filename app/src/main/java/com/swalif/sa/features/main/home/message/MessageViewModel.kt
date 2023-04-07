package com.swalif.sa.features.main.home.message

import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.*
import com.swalif.sa.CHANNEL_ID_ARG
import com.swalif.sa.MY_UID_ARG
import com.swalif.sa.core.storage.FilesManager
import com.swalif.sa.mapper.toMessageList
import com.swalif.sa.model.*
import com.swalif.sa.repository.messageRepository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import logcat.logcat
import java.time.LocalDateTime
import javax.inject.Inject

// TODO: add onEvent Function 1- onNewMessage 2- onSendMessage 3- onNavigate
@HiltViewModel
class MessageViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(MessageState())
    private val channelID = savedStateHandle.get<Int>(CHANNEL_ID_ARG)!!
    private val myUid = savedStateHandle.get<String>(MY_UID_ARG)!!
    private val chatInfo = MutableStateFlow(ChatInfo())
    val state = combine(
        _state,
        messageRepository.getMessages(channelID),
        chatInfo
    ) { state, message, chatInfo ->
        state.copy(
            message.messages.toMessageList().sortedByDescending {
                it.dateTime
            },
            chatInfo = chatInfo,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MessageState())


    init {
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.readMessages(channelID)
        }
        viewModelScope.launch(Dispatchers.IO) {
            chatInfo.update {
                ChatInfo(
                    "salman",
                    imageUri = "https://i.pinimg.com/originals/b4/c1/fb/b4c1fbf0e913bf9365c8fa0dcc48c0c0.jpg",
                    userStatus = UserStatus.Offline(LocalDateTime.now().minusDays(1))
                )
            }
        }

    }
    private var isMe = true
    fun sendTextMessage(message: String) {
        val i = if (isMe) {
            "test"
        } else {
            "salman"
        }
        isMe = !isMe
        val message1 = Message(
            0, channelID, i, message, LocalDateTime.now(), null,MessageStatus.SEEN,MessageType.TEXT
        )
        sendMessage(message1)
        removeTexts()
    }
    fun sendImage(imageUri:String){
        val i = if (isMe) {
            "test"
        } else {
            "salman"
        }
        isMe = !isMe
        val message1 = Message(
            0, channelID, i, "", LocalDateTime.now(), imageUri,MessageStatus.SEEN,MessageType.IMAGE
        )
        sendMessage(message1)
    }
    private fun sendMessage(message:Message){
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.addMessage(message)
        }
    }
    private fun removeTexts() {
        _state.update {
            it.copy(
                text = ""
            )
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
