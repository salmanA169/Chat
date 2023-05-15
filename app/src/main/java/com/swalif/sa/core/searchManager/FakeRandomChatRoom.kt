package com.swalif.sa.core.searchManager

import com.swalif.sa.datasource.remote.firestore_dto.UserDto
import com.swalif.sa.datasource.remote.firestore_dto.UsersDto

class FakeRandomChatRoom : AbstractChatRoom() {
    override var users: List<UsersDto>
        get() = TODO("Not yet implemented")
        set(value) {}
    override var roomStatus: RoomStatus
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun getRoomEvent(): RoomEvent {
        TODO("Not yet implemented")
    }

    override val maxUsers: Int
        get() = 2
    private val uidUsers = mutableListOf<String>()

    override fun getUIDUsersInRoom(): List<String> {
        return uidUsers
    }


    override fun addUser(UserInfoDto: UserDto) {

//        _roomEvent.update {
//            it.copy(
//                users = it.users + Users(userInfo, UserState.IDLE)
//            )
//        }
//        uidUsers.add(userInfo.uidUser)
//        if (_roomEvent.value.users.size == maxUsers) {
//            _roomEvent.update {
//                it.copy(
//                    roomStatus = RoomStatus.COMPLETE_USERS
//                )
//            }
//        } else if (_roomEvent.value.users.size > maxUsers) {
//            throw RoomMaxUsersExceptions("users more then max users, currentUsers: ${_roomEvent.value.users.size} , maxUsers: $maxUsers")
//        }
    }

    override fun updateUserStatus(userUid: String, userState: UserState) {
//        val findUser = _roomEvent.value.users.find { it.userInfo.uidUser == userUid }!!
//        val updateUser = findUser.copy(userState = userState)
//        val users = _roomEvent.value.users.toMutableList()
//        val getUserIndex = users.indexOf(findUser)
//        users[getUserIndex] = updateUser
//        _roomEvent.update {
//            it.copy(
//                users = users,
//                startChatRoom = users.all{ it.isAccept() }
//            )
//        }
    }

    override fun removeUser(UserInfoDto: UserDto) {
//        val users = _roomEvent.value.users
//        val findUsers = users.find { it.userInfo == userInfo }
//        _roomEvent.update {
//            it.copy(
//                users = users - findUsers!!
//            )
//        }
//        uidUsers.remove(userInfo.uidUser)
//        if (users.size <= maxUsers) {
//            _roomEvent.update {
//                it.copy(
//                    roomStatus = RoomStatus.WAITING_USERS
//                )
//            }
//        } else if (users.size < maxUsers) {
//            throw RoomMaxUsersExceptions("users more then max users, currentUsers: ${users.size} , maxUsers: $maxUsers")
//        }
    }

    override fun isBothUsersAccepts(): Boolean {
//        return _roomEvent.value.users.all { it.userState == UserState.ACCEPT }
        return false
    }
}