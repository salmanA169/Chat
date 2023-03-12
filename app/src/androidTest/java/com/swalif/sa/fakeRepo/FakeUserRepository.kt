package com.swalif.sa.fakeRepo

import com.swalif.sa.datasource.local.dao.UserDao
import com.swalif.sa.datasource.local.entity.UserEntity
import com.swalif.sa.mapper.UserInfo
import com.swalif.sa.mapper.toUserInfo
import com.swalif.sa.repository.userRepository.UserRepository
import kotlinx.coroutines.flow.Flow
import logcat.logcat
import javax.inject.Inject

class FakeUserRepository @Inject constructor(
    private val userDao: UserDao
):UserRepository {
    override suspend fun insertUser(user: UserEntity) {
        logcat {
            "called insertion"
        }
        userDao.insertUser(user)
    }

    override suspend fun deleteUser(user: UserEntity) {
        userDao.deleteUser(user)
    }

    override suspend fun getUserByUid(uid: String): UserInfo? {
        println("called get user")
        return userDao.getUserByUid(uid)?.toUserInfo()
    }

    override fun isUserAvailable(): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}