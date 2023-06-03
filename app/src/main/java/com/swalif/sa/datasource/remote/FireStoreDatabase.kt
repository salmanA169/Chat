package com.swalif.sa.datasource.remote

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.swalif.Constants
import com.swalif.sa.datasource.remote.firestore_dto.ChatDto
import com.swalif.sa.datasource.remote.firestore_dto.UserDto
import com.swalif.sa.datasource.remote.firestore_dto.UserStatusDto
import com.swalif.sa.datasource.remote.firestore_dto.toUserChat
import com.swalif.sa.mapper.toUserInfo
import com.swalif.sa.model.Chat
import com.swalif.sa.model.UserInfo
import kotlinx.coroutines.tasks.await
import logcat.logcat
import java.util.UUID
import javax.inject.Inject

class FireStoreDatabase @Inject constructor(
    private val fireStoreDatabase: FirebaseFirestore
) {


    suspend fun getChats(myUid: String): List<Chat> {
        val chats = fireStoreDatabase.collection(Constants.CHATS_COLLECTIONS)
            .whereEqualTo("acceptRequestFriends", false).get().await()
            .toObjects(ChatDto::class.java)
        val filterChat = chats.filter {
            it.users.find { it.userUid == myUid } != null
        }

        return filterChat.map {
            val user = it.users.find { it.userUid != myUid }
            Chat(
                it.chatId,
                "",
                user!!.username,
                "${user.username} wants to chat with you",
                user.image,
                0,
                0,"",2
            )
        }
    }

    /**
     * @return chat id
     *
     */
    suspend fun createNewChatIfNotExist(myUser: UserInfo, user: UserInfo): String {
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
            return filterChat.first().chatId
        }
        val roomId = UUID.randomUUID().toString()
        val createNewChat = ChatDto(
            listOf(myUser.toUserChat(), user.toUserChat()), 2, false, roomId
        )
        fireStoreDatabase.collection(Constants.CHATS_COLLECTIONS).add(createNewChat).await()
        return roomId
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