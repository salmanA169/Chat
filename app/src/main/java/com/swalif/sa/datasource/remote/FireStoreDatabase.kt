package com.swalif.sa.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.swalif.Constants
import com.swalif.sa.datasource.remote.firestore_dto.UserDto
import kotlinx.coroutines.tasks.await
import java.io.Closeable
import javax.inject.Inject

class FireStoreDatabase  @Inject constructor(
    private val fireStoreDatabase: FirebaseFirestore
) {

    suspend fun saveUserFirstTime(userInfo:UserDto):Boolean{
        return try {
            fireStoreDatabase.collection(Constants.USERS_COLLECTIONS).add(userInfo).await()
            true
        }catch (e:Exception){
             false
        }
    }
}