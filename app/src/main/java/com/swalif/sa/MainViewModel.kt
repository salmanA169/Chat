package com.swalif.sa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swalif.sa.component.Gender
import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.datasource.local.entity.UserEntity
import com.swalif.sa.datasource.remote.FireStoreDatabase
import com.swalif.sa.model.UserInfo
import com.swalif.sa.repository.chatRepositoy.ChatRepository
import com.swalif.sa.repository.messageRepository.MessageRepository
import com.swalif.sa.repository.userRepository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userRepository: UserRepository,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    @Inject lateinit var dispatcherProvider: DispatcherProvider
    private val userRepo = userRepository
    val isUserAvailable = userRepo.isUserAvailable()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000), null
        )


     fun setOffline(){
         viewModelScope.launch(dispatcherProvider.io) {
             userRepo.setOffline()
         }
    }

    fun nukeChatAndMessageTables(){
        viewModelScope.launch(dispatcherProvider.io) {
            // TODO: later remove all files and move it later to work manager
            chatRepository.nukeTable()
            messageRepository.nukeMessageTable()
        }
    }
    fun setOnline(){
        viewModelScope.launch(dispatcherProvider.io) {
            userRepo.setOnline()
        }
    }
}
