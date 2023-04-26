package com.swalif.sa.core.searchManager

import com.swalif.sa.core.searchManager.exceptions.RoomMaxUsersExceptions
import com.swalif.sa.model.UserInfo
import kotlinx.coroutines.flow.*

class FakeRandomChatRoom : AbstractChatRoom() {
    private val _roomEvent = MutableStateFlow(RoomEvent())
    override val roomEvent: StateFlow<RoomEvent>
        get() = _roomEvent.asStateFlow()
    override val maxUsers: Int
        get() = 2
    private val uidUsers = mutableListOf<String>()

    override fun getUIDUsersInRoom(): List<String> {
        return uidUsers
    }

    override fun addUser(userInfo: UserInfo) {

        _roomEvent.update {
            it.copy(
                users = it.users + Users(userInfo, UserState.IDLE)
            )
        }
        uidUsers.add(userInfo.userUid)
        if (_roomEvent.value.users.size == maxUsers) {
            _roomEvent.update {
                it.copy(
                    roomStatus = RoomStatus.COMPLETE_USERS
                )
            }
        } else if (_roomEvent.value.users.size > maxUsers) {
            throw RoomMaxUsersExceptions("users more then max users, currentUsers: ${_roomEvent.value.users.size} , maxUsers: $maxUsers")
        }
    }

    override fun updateUserStatus(userUid: String, userState: UserState) {
        val findUser = _roomEvent.value.users.find { it.userInfo.userUid == userUid }!!
        val updateUser = findUser.copy(userState = userState)
        val users = _roomEvent.value.users.toMutableList()
        val getUserIndex = users.indexOf(findUser)
        users[getUserIndex] = updateUser
        _roomEvent.update {
            it.copy(
                users = users,
                startChatRoom = users.all{ it.isAccept() }
            )
        }
    }

    override fun removeUser(userInfo: UserInfo) {
        val users = _roomEvent.value.users
        val findUsers = users.find { it.userInfo == userInfo }
        _roomEvent.update {
            it.copy(
                users = users - findUsers!!
            )
        }
        uidUsers.remove(userInfo.userUid)
        if (users.size <= maxUsers) {
            _roomEvent.update {
                it.copy(
                    roomStatus = RoomStatus.WAITING_USERS
                )
            }
        } else if (users.size < maxUsers) {
            throw RoomMaxUsersExceptions("users more then max users, currentUsers: ${users.size} , maxUsers: $maxUsers")
        }
    }

    override fun isBothUsersAccepts(): Boolean {
        return _roomEvent.value.users.all { it.userState == UserState.ACCEPT }
    }
}