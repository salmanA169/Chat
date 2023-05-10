package com.swalif.sa.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.swalif.Constants
import com.swalif.sa.datasource.remote.firestore_dto.UserDto
import kotlinx.coroutines.tasks.await
import logcat.logcat
import java.io.Closeable
import javax.inject.Inject

class FireStoreDatabase @Inject constructor(
    private val fireStoreDatabase: FirebaseFirestore
) {

    suspend fun saveUserFirstTime(userInfo: UserDto): Boolean {
        return try {
            fireStoreDatabase.collection(Constants.USERS_COLLECTIONS).add(userInfo).await()
            true
        } catch (e: Exception) {
            false
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