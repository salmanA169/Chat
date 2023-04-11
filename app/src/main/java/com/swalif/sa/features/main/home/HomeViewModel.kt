package com.swalif.sa.features.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.mapper.toChat
import com.swalif.sa.mapper.toChatEntity
import com.swalif.sa.mapper.toMessageList
import com.swalif.sa.mapper.toMessageModel
import com.swalif.sa.model.Chat
import com.swalif.sa.repository.chatRepositoy.ChatRepository
import com.swalif.sa.repository.messageRepository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import logcat.logcat
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val dispatchers :DispatcherProvider
) : ViewModel() {
    val homeState = chatRepository.getChats().map {
        HomeChatState(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeChatState())

    fun addTestChat(chat: Chat) {
        viewModelScope.launch(dispatchers.io) {
            chatRepository.insertChat(
                chat
            )
        }
    }

    init {
        viewModelScope.launch(dispatchers.io) {
            checkChatIsDeletedMessages()
        }
    }
    // TODO: move it to workManager
     private suspend fun checkChatIsDeletedMessages(){
        val chatsIDs = chatRepository.getChats().first().distinctBy {
            it.chatId
        }.map {
            it.chatId
        }
        val messagesChatsIds = messageRepository.getMessages().distinctBy {
            it.chatId
        }.map {
            it.chatId
        }
        val ids = mutableSetOf<Int>()
        for (i in messagesChatsIds){
            if (!chatsIDs.contains(i)){
                ids.add(i)
            }
        }
        ids.forEach {id->
            val getMessage = messageRepository.getMessages().filter {
                it.chatId == id
            }
            messageRepository.deleteMessages(getMessage)
        }
    }
    fun deleteChatById(chatId: Int) {
        viewModelScope.launch(dispatchers.io) {
            val getChat = messageRepository.getMessages(chatId).filter {
                it.chat.chatId == chatId
            }.first()
            chatRepository.deleteChatById(getChat.chat.toChat())
            messageRepository.deleteMessages(getChat.messages.toMessageList())
        }
    }
}