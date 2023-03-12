package com.swalif.sa.repository.userRepository

import com.swalif.sa.datasource.local.entity.UserEntity
import com.swalif.sa.model.LoginInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun insertUser(user:UserEntity)
    suspend fun deleteUser(user:UserEntity)
    suspend fun getUserByUid(uid:String):LoginInfo?
    fun isUserAvailable():Flow<Boolean>
}