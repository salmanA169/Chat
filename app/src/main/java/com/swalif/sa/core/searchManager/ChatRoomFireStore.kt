package com.swalif.sa.core.searchManager

import com.google.firebase.firestore.DocumentReference
import com.swalif.sa.core.searchManager.exceptions.RoomMaxUsersExceptions
import com.swalif.sa.coroutine.DispatcherProviderImpl
import com.swalif.sa.datasource.remote.firestore_dto.UserDto
import com.swalif.sa.datasource.remote.firestore_dto.UsersDto
import com.swalif.sa.datasource.remote.firestore_dto.toUsers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

interface FireStoreRoomEvent {
    fun onAllUserJoinChat(roomId: String)
}

class ChatRoomFireStore(
    val currentRoomDocumentReference: DocumentReference,
    private val firestoreRoomEvent: FireStoreRoomEvent
) : AbstractChatRoom() {


    override fun deleteRoom() {
        currentRoomDocumentReference.delete()
    }

    override var users: List<UsersDto> = mutableListOf()
    override var roomStatus: RoomStatus = RoomStatus.WAITING_USERS

    private val dispatcherProvider = DispatcherProviderImpl()
    private var currentJob: Job? = null
    private val coroutineScope = CoroutineScope(SupervisorJob() + dispatcherProvider.io)
    override fun getRoomEvent(): RoomEvent {
        return RoomEvent(
            users.toUsers(), roomStatus, isBothUsersAccepts()
        )
    }

    constructor(
        roomId: String,
        users: List<UsersDto>,
        roomStatus: RoomStatus,
        roomDocumentReference: DocumentReference,
        firestoreRoomEvent: FireStoreRoomEvent
    ) : this(roomDocumentReference, firestoreRoomEvent) {
        this.roomId = roomId
        this.users = users
        this.roomStatus = roomStatus
    }

    override val maxUsers: Int
        get() = 2


    override fun addUser(UserInfoDto: UserDto) {
        currentJob?.cancel()
        val currentUsers = users.distinctBy { it.user.uidUser }
        val checkUser = currentUsers.find { it.user.uidUser == UserInfoDto.uidUser }
        if (checkUser != null) {
            return
        }

        if (currentUsers.size < maxUsers) {
            val newUsers = currentUsers + UsersDto(UserInfoDto, UserState.IDLE)
            users = newUsers

            if (users.size == maxUsers) {
                roomStatus = RoomStatus.COMPLETE_USERS
            } else {
                roomStatus = RoomStatus.WAITING_USERS
            }
            currentJob = coroutineScope.launch {
                val data = mapOf(
                    "users" to users,
                    "roomStatus" to roomStatus,
                )
                currentRoomDocumentReference.update(data).await()
            }
        } else {
            throw RoomMaxUsersExceptions("users more then max users, currentUsers: ${users.size} , maxUsers: $maxUsers")
        }

    }

    override fun removeUser(UserInfoDto: UserDto) {
        val findUsers = users.find { it.user.uidUser == UserInfoDto.uidUser }!!
        val updateList = users - findUsers
        users = updateList
    }

    override fun isBothUsersAccepts(): Boolean {
        return users.all { it.userState == UserState.ACCEPT }
    }

    override fun updateUserStatus(userUid: String, userState: UserState) {
        val getCurrentUser = users.find { it.user.uidUser == userUid }!!
        val oldMyStatus = getCurrentUser.userState
        val getIndex = users.indexOf(getCurrentUser)
        val newData = users.toMutableList()
        val updatedUser = getCurrentUser.copy(userState = userState)
        newData[getIndex] = updatedUser
        users = newData
        val data = mapOf("users" to newData, "shouldStartChat" to isBothUsersAccepts())
        currentJob?.cancel()
        currentJob = coroutineScope.launch {
            if (isBothUsersAccepts()) {
                firestoreRoomEvent.onAllUserJoinChat(roomId)
            }
            if (users.all { it.userState == UserState.IGNORE }) {
                deleteRoom()
            } else if (oldMyStatus != UserState.ACCEPT) {
                currentRoomDocumentReference.update(data).await()
            }
        }

    }

    override fun getUIDUsersInRoom(): List<String> {
        return users.map { it.user.uidUser }
    }
}