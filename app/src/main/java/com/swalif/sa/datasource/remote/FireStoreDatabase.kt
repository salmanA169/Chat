package com.swalif.sa.datasource.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.swalif.Constants
import com.swalif.sa.datasource.remote.firestore_dto.UserDto
import com.swalif.sa.datasource.remote.firestore_dto.UserStatusDto
import com.swalif.sa.mapper.toUserInfo
import com.swalif.sa.model.UserInfo
import com.swalif.sa.model.UserStatus
import kotlinx.coroutines.tasks.await
import logcat.logcat
import java.io.Closeable
import javax.inject.Inject

class FireStoreDatabase @Inject constructor(
    private val fireStoreDatabase: FirebaseFirestore
) {

    suspend fun getUsers():List<UserInfo>{
        val collectionUsers = fireStoreDatabase.collection(Constants.USERS_COLLECTIONS).get().await()
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

   suspend  fun setOffline(user:UserDto){
        fireStoreDatabase.collection(Constants.USERS_COLLECTIONS).whereEqualTo("uidUser" , user.uidUser).get().await().apply {
            val document = firstOrNull()
            val data = mapOf("lastSeen" to FieldValue.serverTimestamp(),"userStatus" to  UserStatusDto.OFFLINE)
            document?.let{
                it.reference.update(data).await()
            }
        }

    }

    suspend  fun setOnline(user:UserDto){
        fireStoreDatabase.collection(Constants.USERS_COLLECTIONS).whereEqualTo("uidUser" , user.uidUser).get().await().apply {
            val document = firstOrNull()
            val data = mapOf("lastSeen" to FieldValue.serverTimestamp(),"userStatus" to  UserStatusDto.ONLINE)
            document?.let{
                it.reference.update(data).await()
            }
        }

    }
    suspend fun getUserByUid(uidUser: String): UserDto? {
        val users = fireStoreDatabase.collection(Constants.USERS_COLLECTIONS)
            .whereEqualTo("uidUser", uidUser).get().await().toObjects(UserDto::class.java).run {
                logcat("Firestore: GetUserByUid"){
                    toString()
                }
                firstOrNull()
            }
        return users
    }
}