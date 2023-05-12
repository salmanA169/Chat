package com.swalif.sa.core.searchManager

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.swalif.sa.core.searchManager.exceptions.RoomMaxUsersExceptions
import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.datasource.remote.FireStoreDatabase
import com.swalif.sa.model.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import logcat.logcat
import javax.inject.Inject

class ChatRoomFireStore() : AbstractChatRoom() {

    constructor(roomId:String,users:List<Users>,roomStatus: RoomStatus):this(){
        this.roomId = roomId
        _roomEvent.update {
            it.copy(users,roomStatus)
        }
    }
    private val _roomEvent = MutableStateFlow(RoomEvent())
    override val roomEvent: StateFlow<RoomEvent>
        get() = _roomEvent.asStateFlow()
    override val maxUsers: Int
        get() = 2

    override fun addUser(userInfo: UserInfo) {

        val currentUsers = _roomEvent.value.users.distinctBy { it.userInfo.uidUser }
        val checkUser = currentUsers.find { it.userInfo.uidUser == userInfo.uidUser }
        if (checkUser != null) {
            return
        }

        if (currentUsers.size < maxUsers) {
            val newUsers = currentUsers + Users(userInfo, UserState.IDLE)

            val currentStatus = if (newUsers.size == maxUsers) {
                RoomStatus.COMPLETE_USERS
            } else {
                RoomStatus.WAITING_USERS
            }
            _roomEvent.update {
                it.copy(
                    newUsers, currentStatus
                )
            }
        } else {
            throw RoomMaxUsersExceptions("users more then max users, currentUsers: ${_roomEvent.value.users.size} , maxUsers: $maxUsers")
        }

    }

    override fun removeUser(userInfo: UserInfo) {
        val users = _roomEvent.value.users
        val findUsers = users.find { it.userInfo.uidUser == userInfo.uidUser }!!
        val updateList = users - findUsers

        _roomEvent.update {
            it.copy(
                updateList
            )
        }


    }

    override fun isBothUsersAccepts(): Boolean {
        TODO("Not yet implemented")
    }

    override fun updateUserStatus(userUid: String, userState: UserState) {
        val users = _roomEvent.value.users
        val getCurrentUser = users.find { it.userInfo.uidUser == userUid }!!
        val getIndex = users.indexOf(getCurrentUser)
        val newData = users.toMutableList()
        val updatedUser = getCurrentUser.copy(userState = userState)
        newData[getIndex] = updatedUser

        _roomEvent.update {
            it.copy(
                newData
            )
        }


    }

    override fun getUIDUsersInRoom(): List<String> {
        TODO("Not yet implemented")
    }
}