package com.swalif.sa.core.searchManager

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.swalif.Constants
import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.datasource.remote.firestore_dto.RoomResultDto
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
):SearchManager {


    override var onSearchEventListener: SearchEvent?= null

    private var currentRoom :AbstractChatRoom? = null
    private var currentJob:Job?= null
    private val currentScope = CoroutineScope(SupervisorJob() + dispatcherProvider.default)
    private var currentUserInfo :UserInfo? = null
    private var currentSnapshotListener : ListenerRegistration? = null
    override fun deleteRoom(roomId: String) {
        TODO("Not yet implemented")
    }

    override fun registerSearchEvent(userInfo: UserInfo) {
        currentJob?.cancel()
        currentJob = currentScope.launch {
            val document = firestore.collection(Constants.ROOMS_COLLECTIONS).get().await()
            val getRooms = document.toObjects(RoomResultDto::class.java)

            val findMyRoom = getRooms.find { it.maxUsers > it.users.size }
            if (findMyRoom == null){
                val newDocument = firestore.collection(Constants.ROOMS_COLLECTIONS).document()
                val newRoom = ChatRoomFireStore()
                val data = RoomResultDto(listOf(UsersDto(userInfo.toUserDto(),UserState.IDLE)),newRoom.maxUsers, roomId = newRoom.roomId)
                newDocument.set(data).await()
                currentRoom = newRoom
                currentUserInfo = userInfo
                currentSnapshotListener = listenRoomEvent(newDocument)
            }else{
                val getCurrentDocument = firestore.collection(Constants.ROOMS_COLLECTIONS).whereEqualTo("roomId",findMyRoom.roomId).get().await()
                val getRoom = getCurrentDocument.firstOrNull()
                val toRoom = getRoom!!.toObject(RoomResultDto::class.java)
                currentRoom = ChatRoomFireStore(toRoom.roomId,toRoom.users.toUsers(),toRoom.roomStatus)
                currentUserInfo = userInfo
                currentSnapshotListener = listenRoomEvent(getRoom.reference)
                val newData = toRoom.users.toMutableList() + UsersDto(userInfo.toUserDto(),UserState.IDLE)
                logcat("SearchManager: new Data"){
                    "$newData"
                }
                val data = mapOf("users" to newData)
                getRoom.reference.update(data).await()
            }
        }
    }

    private fun listenRoomEvent(documentReference: DocumentReference):ListenerRegistration{
        return documentReference.addSnapshotListener { value, error ->
            val roomEvent = value?.toObject(RoomResultDto::class.java)!!
            logcat("SearchManager observer"){
                "called ith new data $roomEvent" +
                        ""
            }
            roomEvent.users.forEach {
                currentRoom!!.addUser(it.user.toUserInfo())
            }
        }
    }
    override fun unregisterSearchEvent() {
        currentJob?.cancel()
        currentJob = null
        currentRoom = null
        currentUserInfo = null
        currentSnapshotListener?.remove()
        currentSnapshotListener = null

    }

    override fun updateUserStatus(userState: UserState) {

    }

    override fun reload() {

    }

    override fun close() {
        unregisterSearchEvent()
    }
}