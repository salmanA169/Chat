package com.swalif.sa.features.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.datasource.remote.FireStoreDatabase
import com.swalif.sa.mapper.toChatEntity
import com.swalif.sa.mapper.toMessageList
import com.swalif.sa.model.Chat
import com.swalif.sa.model.UserInfo
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
    private val dispatchers: DispatcherProvider,
    private val userRepository: UserRepository,
    private val firebaseDatabase: FireStoreDatabase
) : ViewModel() {
    private var myUser: UserInfo? = null
    private val _homeState = MutableStateFlow(HomeChatState())
    val homeState = _homeState.asStateFlow()

    private fun syncChats() {
        val getChats = firebaseDatabase.getChats(myUser?.uidUser ?: return)
        // TODO: fix it duplicated chat and throw exception because chat id 
        viewModelScope.launch(dispatchers.io) {
            getChats.collect{chats->
                _homeState.update {
                    it.copy(
                        it.chats + chats
                    )
                }

            }
        }
    }

    init {
        viewModelScope.launch(dispatchers.io) {
            checkChatIsDeletedMessages()
        }
        viewModelScope.launch(dispatchers.io) {
            val getMyUser = userRepository.getCurrentUser() ?: return@launch
            myUser = getMyUser
            _homeState.update {
                it.copy(
                    myUid = getMyUser.uidUser
                )
            }
            syncChats()

        }
        viewModelScope.launch(dispatchers.io) {
            chatRepository.getChats().collect { chats ->
                _homeState.update {
                    it.copy(
                        chats
                    )
                }
            }
        }

    }

    // TODO: move it to workManager
    private suspend fun checkChatIsDeletedMessages() {
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
        for (i in messagesChatsIds) {
            if (!chatsIDs.contains(i)) {
                ids.add(i)
            }
        }
        ids.forEach { id ->
            val getMessage = messageRepository.getAllMessages().filter {
                it.chatId == id
            }
            messageRepository.deleteMessages(getMessage)
        }
    }

    fun deleteChatById(chatId: String) {
        viewModelScope.launch(dispatchers.io) {
            val getChat = chatRepository.getChatById(chatId)
            if (getChat != null) {
                chatRepository.deleteChatById(getChat.toChatEntity())
                messageRepository.deleteMessagesByChatId(chatId)
            }
            firebaseDatabase.deleteChatById(myUser!!.uidUser, chatId)
        }
    }
}