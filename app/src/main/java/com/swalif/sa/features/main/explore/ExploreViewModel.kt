package com.swalif.sa.features.main.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.datasource.remote.FireStoreDatabase
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
     dispatchProvider :DispatcherProvider,
    private val userRepository: UserRepository
):ViewModel() {

    private val _state = MutableStateFlow(ExploreState())
    val state = _state.asStateFlow()

    private var myUid :String? = null

    init {

        viewModelScope.launch(dispatchProvider.io) {
            val getMyUser = userRepository.getCurrentUser()
            myUid = getMyUser!!.uidUser
            val getUsers = firebaseDatabase.getUsers()
            _state.update {
                it.copy(
                    getUsers.filter { it.uidUser != myUid!! }
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        logcat{
            "called on cleadred"
        }
    }
}
