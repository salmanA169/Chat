package com.swalif.sa.fakeRepo

import android.content.Intent
import android.content.IntentSender
import com.swalif.sa.datasource.local.dao.UserDao
import com.swalif.sa.datasource.local.entity.UserEntity
import com.swalif.sa.mapper.toUserEntity
import com.swalif.sa.model.SignInResult
import com.swalif.sa.model.UserInfo
import com.swalif.sa.repository.userRepository.UserRepository
import kotlinx.coroutines.flow.Flow
import logcat.logcat
import javax.inject.Inject

class FakeUserRepository @Inject constructor(
    private val userDao: UserDao
):UserRepository {
    override suspend fun insertUser(user: UserInfo) {
        TODO("Not yet implemented")
    }

    override suspend fun saveUser(user: UserInfo) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(user: UserInfo) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByUid(uid: String): UserInfo? {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentUser(): UserInfo? {
        TODO("Not yet implemented")
    }

    override suspend fun signIn(): IntentSender? {
        TODO("Not yet implemented")
    }

    override suspend fun getSignInResult(intent: Intent): SignInResult {
        TODO("Not yet implemented")
    }

    override suspend fun signOut(uid: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setOffline() {
        TODO("Not yet implemented")
    }

    override suspend fun setOnline() {
        TODO("Not yet implemented")
    }

    override fun isUserAvailable(): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}