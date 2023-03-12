package com.swalif.sa.features.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swalif.sa.model.Chat
import com.swalif.sa.repository.chatRepositoy.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    val homeState = chatRepository.getChats().map {
        HomeChatState(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeChatState())

    fun addTestChat(chat: Chat) {
        viewModelScope.launch {
            chatRepository.insertChat(
                chat
            )
        }
    }
}