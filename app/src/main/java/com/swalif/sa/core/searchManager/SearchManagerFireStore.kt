package com.swalif.sa.core.searchManager

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.snapshots
import com.swalif.Constants
import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.datasource.remote.firestore_dto.ChatDto
import com.swalif.sa.datasource.remote.firestore_dto.RoomResultDto
import com.swalif.sa.datasource.remote.firestore_dto.UsersChatDto
import com.swalif.sa.datasource.remote.firestore_dto.UsersDto
import com.swalif.sa.datasource.remote.firestore_dto.toUsers
import com.swalif.sa.mapper.toUserDto
import com.swalif.sa.mapper.toUserInfo
import com.swalif.sa.model.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import logcat.logcat
import javax.inject.Inject

class SearchManagerFireStore @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val dispatcherProvider: DispatcherProvider
) : SearchManager ,FireStoreRoomEvent{


    override fun onAllUserJoinChat(roomId: String) {
        currentScope.launch {
            val users = currentRoom!!.users.map {
                UsersChatDto(it.user.uidUser,false,false,it.user.photoUri,it.user.userName)
            }
            val maxUsers = currentRoom!!.maxUsers
            val usersUids = currentRoom!!.getUIDUsersInRoom()
            val chatsWithUsers = ChatDto(users,maxUsers,false,roomId)
            firestore.collection(Constants.CHATS_COLLECTIONS).add(chatsWithUsers).await()

            deleteRoom(roomId)
        }
    }

    override var onSearchEventListener: SearchEvent? = null

    private var currentRoom: AbstractChatRoom? = null
    private var currentJob: Job? = null
    private val currentScope = CoroutineScope(SupervisorJob() + dispatcherProvider.io)
    private var currentUserInfo: UserInfo? = null
    private var currentSnapshotListener: ListenerRegistration? = null
    override  suspend fun deleteRoom(roomId: String) {
        val firestoreRoom = currentRoom as? ChatRoomFireStore
        firestoreRoom?.let {
            it.currentRoomDocumentReference.delete()
        }
    }

    override fun registerSearchEvent(userInfo: UserInfo) {
        currentJob?.cancel()
        currentJob = currentScope.launch {
            val document = firestore.collection(Constants.ROOMS_COLLECTIONS).get().await()
            val getRooms = document.toObjects(RoomResultDto::class.java)

            val findMyRoom = getRooms.find { it.maxUsers > it.users.size }
            if (findMyRoom == null) {
                val newDocument = firestore.collection(Constants.ROOMS_COLLECTIONS).document()
                val newRoom = ChatRoomFireStore(newDocument,this@SearchManagerFireStore)
                val data = RoomResultDto(
                    listOf(),
                    newRoom.maxUsers,
                    roomId = newRoom.roomId
                )
                newDocument.set(data).await()
                currentRoom = newRoom
                currentUserInfo = userInfo
                currentSnapshotListener = listenRoomEvent(newDocument)
                currentRoom!!.addUser(userInfo.toUserDto())
            } else {
                val getCurrentDocument = firestore.collection(Constants.ROOMS_COLLECTIONS)
                    .whereEqualTo("roomId", findMyRoom.roomId).get().await()
                val getRoom = getCurrentDocument.firstOrNull()
                val toRoom = getRoom!!.toObject(RoomResultDto::class.java)
                currentRoom =
                    ChatRoomFireStore(toRoom.roomId, toRoom.users, toRoom.roomStatus,getRoom.reference,this@SearchManagerFireStore)
                currentUserInfo = userInfo
                currentSnapshotListener = listenRoomEvent(getRoom.reference)
                currentRoom!!.addUser(userInfo.toUserDto())
            }
        }
    }

    private fun listenRoomEvent(documentReference: DocumentReference): ListenerRegistration {
        return documentReference.addSnapshotListener { value, error ->
            val roomEvent = value?.toObject(RoomResultDto::class.java)?:return@addSnapshotListener
            currentRoom!!.users = roomEvent.users
            currentRoom!!.roomStatus = roomEvent.roomStatus
            onSearchEventListener?.onEvent(
                RoomEvent(
                    roomEvent.users.toUsers(),
                    roomEvent.roomStatus,
                    roomEvent.shouldStartChat,roomEvent.roomId
                )
            )
        }
    }

    override fun unregisterSearchEvent() {
        // TODO: remove room if he last one
        currentJob?.cancel()
        currentJob = null
        currentRoom = null
        currentUserInfo = null
        currentSnapshotListener?.remove()
        currentSnapshotListener = null

    }

    override fun updateUserStatus(userState: UserState) {
        currentRoom!!.updateUserStatus(currentUserInfo!!.uidUser,userState)
    }

    override fun reload() {

    }

    override fun close() {
        logcat { "close called" }
        unregisterSearchEvent()
    }
}