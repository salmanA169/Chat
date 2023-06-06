package com.swalif.sa.repository.userRepository

import android.content.Intent
import android.content.IntentSender
import com.swalif.sa.datasource.local.entity.UserEntity
import com.swalif.sa.model.SignInResult
import com.swalif.sa.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun insertUser(user:UserInfo)
    suspend fun saveUser(user:UserInfo)
    suspend fun deleteUser(user:UserInfo)
    suspend fun getUserByUid(uid:String):UserInfo?
    suspend fun getCurrentUser(): UserInfo?
    fun isUserAvailable():Flow<Boolean>
    suspend fun signIn():IntentSender?
    suspend fun getSignInResult(intent: Intent):SignInResult
    suspend fun signOut(uid:String)
    suspend fun setOffline()
    suspend fun setOnline()
    suspend fun signIn(email:String,password:String):SignInResult
    suspend fun signUpWithEmailAndPassword(username:String, email:String,password:String,imageUri:String):SignInResult
}