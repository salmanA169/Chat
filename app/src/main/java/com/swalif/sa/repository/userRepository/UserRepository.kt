package com.swalif.sa.repository.userRepository

import android.content.Intent
import android.content.IntentSender
import com.swalif.sa.datasource.local.entity.UserEntity
import com.swalif.sa.model.SignInResult
import com.swalif.sa.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun insertUser(user:UserEntity)
    suspend fun deleteUser(user:UserEntity)
    suspend fun getUserByUid(uid:String):UserInfo?
    suspend fun getCurrentUser(): UserInfo?
    fun isUserAvailable():Flow<Boolean>
    suspend fun signIn():IntentSender?
    suspend fun getSignInResult(intent: Intent):SignInResult
}