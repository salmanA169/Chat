package com.swalif.sa.repository.userRepository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.auth.FirebaseAuth
import com.swalif.sa.core.data_store.getUserUidFlow
import com.swalif.sa.core.data_store.updateIsFirstTime
import com.swalif.sa.datasource.local.dao.UserDao
import com.swalif.sa.datasource.local.entity.UserEntity
import com.swalif.sa.mapper.toUserInfo
import com.swalif.sa.model.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import java.io.Closeable
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val dataStore: DataStore<Preferences>,
    private val firebaseAuth: FirebaseAuth
):UserRepository,FirebaseAuth.AuthStateListener,Closeable {

    private val _currentUserState = MutableStateFlow("")
    override fun onAuthStateChanged(p0: FirebaseAuth) {
        val currentUser = p0.currentUser
        if (currentUser != null){
            _currentUserState.update {
                currentUser.uid
            }
        }else{
            _currentUserState.update {
                ""
            }
        }
    }


    // TODO: implement google sing in
    override suspend fun signIn() {
        TODO("Not yet implemented")
    }

    init {
        firebaseAuth.addAuthStateListener(this)
    }
    override fun close() {
        firebaseAuth.removeAuthStateListener(this)
    }

    override suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
        updateUserStore(user.uidUser)
    }

    override suspend fun deleteUser(user: UserEntity) {
        userDao.deleteUser(user)
        updateUserStore("")
    }

    override suspend fun getUserByUid(uid: String): UserInfo? {
        return userDao.getUserByUid(uid)?.toUserInfo()
    }

    private suspend fun updateUserStore(uid:String) {
        dataStore.updateIsFirstTime(uid)
    }

    override suspend fun getCurrentUser(): UserInfo? {
        val getCurrentUserUid = dataStore.getUserUidFlow().first()
        val getUserInfo = userDao.getUserByUid(getCurrentUserUid)
        return getUserInfo?.toUserInfo()
    }

    override fun isUserAvailable() = combine(_currentUserState,userDao.getUsersFlow()){ userUid, listUsers->
        val hasUid = userUid.isEmpty()
        if (hasUid){
            false
        }else{
            val getCurrentUser = listUsers.find { it.uidUser == userUid }
            getCurrentUser != null
        }
    }
}