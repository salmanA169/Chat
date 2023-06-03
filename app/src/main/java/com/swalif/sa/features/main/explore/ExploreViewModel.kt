package com.swalif.sa.features.main.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swalif.sa.Screens
import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.datasource.remote.FireStoreDatabase
import com.swalif.sa.model.UserInfo
import com.swalif.sa.repository.userRepository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val firebaseDatabase: FireStoreDatabase,
    private val dispatchProvider: DispatcherProvider,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ExploreState())
    val state = _state.asStateFlow()

    private val _event = MutableStateFlow<ExploreEvent?>(null)
    val event = _event.asStateFlow()
    private var myUser: UserInfo? = null

    fun readEvent(){
        _event.update {
            null
        }
    }
    fun onEvent(exploreEvent: UiExploreEvent) {
        when(exploreEvent){
            is UiExploreEvent.NavigateToChat -> {
                viewModelScope.launch(dispatchProvider.io) {
                    val chatId = firebaseDatabase.createNewChatIfNotExist(myUser!!,exploreEvent.userInfo)
                    // TODO: send notification when create new chat
                    _event.update {
                        ExploreEvent.NavigateToChat(chatId,myUser!!.uidUser)
                    }
                }
            }
            UiExploreEvent.NavigateToSearch ->{
                _event.update {
                    ExploreEvent.Navigate(Screens.SearchScreen.route)
                }
            }
        }
    }

    init {
        viewModelScope.launch(dispatchProvider.io) {
            val getMyUser = userRepository.getCurrentUser()
            myUser = getMyUser
            val getUsers = firebaseDatabase.getUsers()
            _state.update {
                it.copy(
                    getUsers.filter { it.uidUser != myUser!!.uidUser }
                )
            }
        }
    }

}
sealed class UiExploreEvent {

    object NavigateToSearch : UiExploreEvent()
    class NavigateToChat(val userInfo:UserInfo):UiExploreEvent()
}
sealed class ExploreEvent {
    class Navigate(val route: String) : ExploreEvent()
    class NavigateToChat(val chatId: String, val myUid: String) : ExploreEvent()
}
