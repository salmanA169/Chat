package com.swalif.sa.features.main.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swalif.sa.core.clipBoard.ClipBoardManager
import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.repository.userRepository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val clipBoardManager: ClipBoardManager
):ViewModel() {
    private val _state = MutableStateFlow(AccountState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            val getUser = userRepository.getCurrentUser()
            _state.update {
                it.copy(
                    getUser
                )
            }
        }
    }

    fun copyText(text:String){
        clipBoardManager.copyText(text)
    }

    fun signOut(uid:String){
        viewModelScope.launch(dispatcherProvider.io) {
            userRepository.signOut(uid)
        }
    }
}