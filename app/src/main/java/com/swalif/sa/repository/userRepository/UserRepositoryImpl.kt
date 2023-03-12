package com.swalif.sa.repository.userRepository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.swalif.sa.core.data_store.getUserUidFlow
import com.swalif.sa.core.data_store.updateIsFirstTime
import com.swalif.sa.datasource.local.dao.UserDao
import com.swalif.sa.datasource.local.entity.UserEntity
import com.swalif.sa.mapper.toUserInfo
import com.swalif.sa.model.LoginInfo
import kotlinx.coroutines.flow.combine
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val dataStore: DataStore<Preferences>
):UserRepository {


    override suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
        updateUserStore(user.uidUser)
    }

    override suspend fun deleteUser(user: UserEntity) {
        userDao.deleteUser(user)
        updateUserStore("")
    }

    override suspend fun getUserByUid(uid: String): LoginInfo? {
        return userDao.getUserByUid(uid)?.toUserInfo()
    }

    private suspend fun updateUserStore(uid:String) {
        dataStore.updateIsFirstTime(uid)
    }

    override fun isUserAvailable() = combine(dataStore.getUserUidFlow(),userDao.getUsersFlow()){ userUid, listUsers->
        val hasUid = userUid.isEmpty()
        if (hasUid){
            false
        }else{
            val getCurrentUser = listUsers.find { it.uidUser == userUid }
            getCurrentUser != null
        }
    }
}