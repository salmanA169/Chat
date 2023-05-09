package com.swalif.sa.fakeRepo

import com.swalif.sa.datasource.local.dao.UserDao
import com.swalif.sa.datasource.local.entity.UserEntity
import com.swalif.sa.mapper.toUserEntity
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

    override suspend fun getUserByUid(uid: String): LoginInfo? {
        println("called get user")
        return userDao.getUserByUid(uid)?.toUserEntity()
    }

    override fun isUserAvailable(): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}