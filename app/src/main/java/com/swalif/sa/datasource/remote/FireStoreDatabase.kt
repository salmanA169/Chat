package com.swalif.sa.datasource.remote

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.swalif.Constants
import com.swalif.sa.datasource.remote.firestore_dto.ChatDto
import com.swalif.sa.datasource.remote.firestore_dto.MessageDto
import com.swalif.sa.datasource.remote.firestore_dto.UserDto
import com.swalif.sa.datasource.remote.firestore_dto.UserStatusDto
import com.swalif.sa.datasource.remote.firestore_dto.toUserChat
import com.swalif.sa.datasource.remote.response.ChatDataResponse
import com.swalif.sa.mapper.toUserInfo
import com.swalif.sa.model.Chat
import com.swalif.sa.model.ChatInfo
import com.swalif.sa.model.MessageStatus
import com.swalif.sa.model.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.tasks.await
import logcat.logcat
import java.util.UUID
import javax.inject.Inject

class FireStoreDatabase @Inject constructor(
    private val fireStoreDatabase: FirebaseFirestore
) {


    suspend fun deleteChatById(myUid: String, chatId: String): Boolean {
        return try {
            val collectionReference = fireStoreDatabase.collection(Constants.CHATS_COLLECTIONS)
                .whereEqualTo("chatId", chatId).get().await()
            val chats = collectionReference.toObjects(ChatDto::class.java)
            if (chats.isNotEmpty()) {
                val chat = chats.first()
                val listUsers = chat.users.toMutableList()
                val getMyUser = listUsers.find { it.userUid == myUid }!!
                val indexMyCurrentUser = listUsers.indexOfFirst { it.userUid == myUid }
                val updateUser = getMyUser.copy(
                    left = true
                )
                listUsers[indexMyCurrentUser] = updateUser
                val documentReference = collectionReference.documents.first().reference.apply {
                    update("users", listUsers).await()
                }

                documentReference.collection(Constants.MESSAGES_COLLECTIONS).add(
                    MessageDto.createAnnouncementMessage(
                        "${getMyUser.username} has left", chatId, getMyUser.userUid
                    )
                )
                if (listUsers.all { it.left }) {
                    documentReference.delete()
                }
            }
            true
        } catch (e: Exception) {
            logcat {
                e.message.toString()
            }
            false
        }
    }

    fun getChats(myUid: String): Flow<List<Chat>> {
        val chats = fireStoreDatabase.collection(Constants.CHATS_COLLECTIONS)
            .whereEqualTo("acceptRequestFriends", false).snapshots()

        return chats.filter {
            val chatsDto = it.toObjects(ChatDto::class.java)

            if (chatsDto.isEmpty()) {
                true
            } else {
                chatsDto.any {
                    it.users.find { users ->
                        users.userUid == myUid
                    } != null
                }
            }
        }.onEach {
            it.documents.forEach { documents ->
                val messageCollection =
                    documents.reference.collection(Constants.MESSAGES_COLLECTIONS).get().await()
                messageCollection.documents.filter {
                    val message = it.toObject<MessageDto>()
                    message != null && message.senderUid != myUid && message.statusMessage != MessageStatus.SEEN
                }.forEach { messageDocuments ->
                    messageDocuments.reference.update("statusMessage", MessageStatus.DELIVERED)
                        .await()
                }
            }
        }.map { it.toObjects(ChatDto::class.java) }.map {
            it.map {
                val user = it.users.find { it.userUid != myUid }
                Chat(
                    it.chatId,
                    "",
                    user!!.username,
                    "${user!!.username} wants to chat with you",
                    user.image,
                    0,
                    0, "", 2
                )
            }
        }
    }

    /**
     * @return chat id
     *
     */
    suspend fun createNewChatIfNotExist(myUser: UserInfo, user: UserInfo): ChatDataResponse {
        val chats = fireStoreDatabase.collection(Constants.CHATS_COLLECTIONS).get().await()
            .toObjects(ChatDto::class.java)
        val filterChat = chats.filter {
            val found = mutableSetOf<String>()
            it.users.forEach {
                found.add(it.userUid)
            }
            found.find { it == myUser.uidUser } != null && found.find { it == user.uidUser } != null
        }
        if (filterChat.isNotEmpty()) {
            return ChatDataResponse(false, filterChat.first().chatId)
        }
        val roomId = UUID.randomUUID().toString()
        val createNewChat = ChatDto(
            listOf(myUser.toUserChat(), user.toUserChat()), 2, false, roomId
        )
        fireStoreDatabase.collection(Constants.CHATS_COLLECTIONS).add(createNewChat).await()
        return ChatDataResponse(true, roomId)
    }

    suspend fun getUsers(): List<UserInfo> {
        val collectionUsers =
            fireStoreDatabase.collection(Constants.USERS_COLLECTIONS).get().await()
        val users = collectionUsers.toObjects<UserDto>()
        return users.map {
            it.toUserInfo()
        }
    }

    suspend fun saveUserFirstTime(userInfo: UserDto): Boolean {
        return try {
            fireStoreDatabase.collection(Constants.USERS_COLLECTIONS).add(userInfo).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun setOffline(user: UserDto) {
        fireStoreDatabase.collection(Constants.USERS_COLLECTIONS)
            .whereEqualTo("uidUser", user.uidUser).get().await().apply {
                val document = firstOrNull()
                val data = mapOf(
                    "lastSeen" to FieldValue.serverTimestamp(),
                    "userStatus" to UserStatusDto.OFFLINE
                )
                document?.let {
                    it.reference.update(data).await()
                }
            }

    }

    suspend fun setOnline(user: UserDto) {
        fireStoreDatabase.collection(Constants.USERS_COLLECTIONS)
            .whereEqualTo("uidUser", user.uidUser).get().await().apply {
                val document = firstOrNull()
                val data = mapOf(
                    "lastSeen" to FieldValue.serverTimestamp(),
                    "userStatus" to UserStatusDto.ONLINE
                )
                document?.let {
                    it.reference.update(data).await()
                }
            }

    }

    suspend fun getUserByUid(uidUser: String): UserDto? {
        val users = fireStoreDatabase.collection(Constants.USERS_COLLECTIONS)
            .whereEqualTo("uidUser", uidUser).get().await().toObjects(UserDto::class.java).run {
                logcat("Firestore: GetUserByUid") {
                    toString()
                }
                firstOrNull()
            }
        return users
    }
}