package com.swalif.sa.features.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.mapper.toChatEntity
import com.swalif.sa.mapper.toMessageList
import com.swalif.sa.model.Chat
import com.swalif.sa.repository.chatRepositoy.ChatRepository
import com.swalif.sa.repository.messageRepository.MessageRepository
import com.swalif.sa.repository.userRepository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val dispatchers :DispatcherProvider,
    private val userRepository: UserRepository
) : ViewModel() {
    private val myUid = MutableStateFlow<String?>(null)
    val homeState = chatRepository.getChats().combine(myUid) {chats,myUid->
        HomeChatState(chats, myUid)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeChatState())

    fun syncChats(){
        // TODO: check chats
    }

    init {
        viewModelScope.launch(dispatchers.io) {
            checkChatIsDeletedMessages()
        }
        viewModelScope.launch(dispatchers.io) {
            myUid.update {
                userRepository.getCurrentUser()?.uidUser
            }
        }
    }
    // TODO: move it to workManager
     private suspend fun checkChatIsDeletedMessages(){
        val chatsIDs = chatRepository.getChats().first().distinctBy {
            it.chatId
        }.map {
            it.chatId
        }
        val messagesChatsIds = messageRepository.getAllMessages().distinctBy {
            it.chatId
        }.map {
            it.chatId
        }
        val ids = mutableSetOf<String>()
        for (i in messagesChatsIds){
            if (!chatsIDs.contains(i)){
                ids.add(i)
            }
        }
        ids.forEach {id->
            val getMessage = messageRepository.getAllMessages().filter {
                it.chatId == id
            }
            messageRepository.deleteMessages(getMessage)
        }
    }
    fun deleteChatById(chatId: String) {
        viewModelScope.launch(dispatchers.io) {
            val getChat = messageRepository.observeMessageByChatId(chatId).filter {
                it.chat.chatId == chatId
            }.first()
            chatRepository.deleteChatById(getChat.chat)
            messageRepository.deleteMessages(getChat.messages.toMessageList())
        }
    }
}