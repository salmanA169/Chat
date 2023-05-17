package com.swalif.sa.features.main.home.message

import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.*
import com.google.firebase.Timestamp
import com.swalif.sa.CHANNEL_ID_ARG
import com.swalif.sa.MY_UID_ARG
import com.swalif.sa.core.storage.FilesManager
import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.datasource.remote.firestore_dto.MessageDto
import com.swalif.sa.mapper.toMessageList
import com.swalif.sa.model.*
import com.swalif.sa.repository.chatRepositoy.ChatRepository
import com.swalif.sa.repository.firestoreChatMessagesRepo.FirestoreChatMessageRepository
import com.swalif.sa.repository.messageRepository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import logcat.logcat
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val firestoreChatMessageRepository: FirestoreChatMessageRepository,
    private val dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(MessageState())
    private val channelID = savedStateHandle.get<String>(CHANNEL_ID_ARG)!!
    private val myUid = savedStateHandle.get<String>(MY_UID_ARG)!!
    private val chatInfo = MutableStateFlow(ChatInfo())

    val state = _state.asStateFlow()

    init {
        firestoreChatMessageRepository.addChatId(channelID)
        viewModelScope.launch(dispatcherProvider.io) {
            firestoreChatMessageRepository.getMessage().collect{messages->
                _state.update {
                    it.copy(
                        messages,
                        myUid = myUid
                    )
                }
            }
        }
        viewModelScope.launch(dispatcherProvider.io) {
            firestoreChatMessageRepository.syncMessages()
        }
    }
    fun sendTextMessage(message: String) {


        val message1 = MessageDto(
            // TODO: fix it to generate
            Random.nextInt(), channelID, myUid, message, Timestamp.now(), null,MessageStatus.SEEN,MessageType.TEXT
        )
        sendMessage(message1)
        removeTexts()
    }
    fun sendImage(imageUri:String){
//        val i = if (isMe) {
//            "test"
//        } else {
//            "salman"
//        }
//        isMe = !isMe
//        val message1 = Message(
//            0, channelID, i, "", LocalDateTime.now(), imageUri,MessageStatus.SEEN,MessageType.IMAGE
//        )
//        sendMessage(message1)
    }
    private fun sendMessage(message:MessageDto){
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

    fun updateMessage(text: String) {
        _state.update {
            it.copy(
                text = text
            )
        }
    }
}
