package com.swalif.sa.features.main.explore.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swalif.sa.core.searchManager.UserState
import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.datasource.local.dao.ChatDao
import com.swalif.sa.datasource.local.entity.ChatEntity
import com.swalif.sa.repository.searchRepository.SearchRepository
import com.swalif.sa.repository.userRepository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val searchRepository: SearchRepository,
    private val dispatcherProvider: DispatcherProvider,
    // for test
private val chatDao :ChatDao
) : ViewModel() {

    private val _searchState = MutableStateFlow<SearchStateUI>(SearchStateUI())
    val searchState = _searchState.asStateFlow()

    fun addTestChat(chatId:Int,username:String,image:String,userUid:String,){
        viewModelScope.launch(dispatcherProvider.io) {
            chatDao.insertChat(ChatEntity(chatId,userUid,username,"test","",image, LocalDateTime.now(),0,""))
        }
    }
    init {
        addCloseable(searchRepository.getClosable())
        viewModelScope.launch {
           val getUser = userRepository.getCurrentUser()

            searchRepository.searchUser(getUser!!).collect{roomEvent->
                _searchState.update {
                    it.copy(
                        roomEvent,
                        getUser
                    )
                }
            }
        }
    }
    fun updateUserStatus(userState: UserState){
        viewModelScope.launch(dispatcherProvider.default) {

            when (userState) {
                UserState.IDLE -> {

                }
                UserState.ACCEPT -> {
                    searchRepository.acceptUser()
                }
                UserState.IGNORE -> {
                    searchRepository.ignoreUser()
                }
                UserState.LEFT -> TODO()
            }
        }
    }
}