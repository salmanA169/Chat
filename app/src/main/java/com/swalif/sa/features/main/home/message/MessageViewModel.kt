package com.swalif.sa.features.main.home.message

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swalif.sa.CHANNEL_ID_ARG
import com.swalif.sa.MY_UID_ARG
import com.swalif.sa.Screens
import com.swalif.sa.mapper.toMessageList
import com.swalif.sa.model.ChatInfo
import com.swalif.sa.model.Message
import com.swalif.sa.model.MessageStatus
import com.swalif.sa.model.UserStatus
import com.swalif.sa.repository.messageRepository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import logcat.logcat
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

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
            chatInfo = chatInfo
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MessageState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(5000)
            chatInfo.update {
                ChatInfo(
                    "salman",
                    imageUri = "https://i.pinimg.com/originals/b4/c1/fb/b4c1fbf0e913bf9365c8fa0dcc48c0c0.jpg",
                    userStatus = UserStatus.Online
                )
            }
            while (true) {
                delay(3000)
                chatInfo.update {
                    it.copy(userStatus = it.userStatus?.getNextStatus())
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        logcat {
            "called"
        }
    }
    private var isMe = true
    fun sendMessage(message: String) {
        val i = if (isMe) {
            "test"
        } else {
            "salman"
        }
        isMe = !isMe
        val message1 = Message(
            0, channelID, i, message, LocalDateTime.now(), MessageStatus.SEEN
        )
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.addMessage(message1)
        }
        removeTexts()
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