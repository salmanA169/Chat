package com.swalif.sa.repository.userRepository

import android.content.Context
import android.content.IntentSender
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.swalif.sa.core.data_store.getUserUidFlow
import com.swalif.sa.core.data_store.updateIsFirstTime
import com.swalif.sa.datasource.local.dao.UserDao
import com.swalif.sa.datasource.local.entity.UserEntity
import com.swalif.sa.mapper.toUserInfo
import com.swalif.sa.model.UserInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import logcat.logcat
import java.io.Closeable
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val dataStore: DataStore<Preferences>,
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : UserRepository, FirebaseAuth.AuthStateListener, Closeable {

    private val _currentUserState = MutableStateFlow("")
    private val _currentAuthState = MutableStateFlow<IntentSender?>(null)
    private val oneTapClint = Identity.getSignInClient(context)
    private val signInRequest = BeginSignInRequest.Builder().setGoogleIdTokenRequestOptions(
        BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
            .setServerClientId("123801215248-c2463dk439ifv2d3uq25ppgmpj7k4a4v.apps.googleusercontent.com")
            .setFilterByAuthorizedAccounts(false).build()
    ).setAutoSelectEnabled(true).build()

    override fun onAuthStateChanged(p0: FirebaseAuth) {
        val currentUser = p0.currentUser
        if (currentUser != null) {
            _currentUserState.update {
                currentUser.uid
            }
        } else {
            _currentUserState.update {
                ""
            }
        }
    }

    override suspend fun signIn() {
        try {
            val result =  oneTapClint.beginSignIn(signInRequest).await()

            _currentAuthState.update {
                result.pendingIntent.intentSender
            }
        }catch (e:Exception){
            logcat {
                e.toString()
            }
        }
    }

    init {
        firebaseAuth.addAuthStateListener(this)
    }

    override fun close() {
        firebaseAuth.removeAuthStateListener(this)
    }

    override fun authState(): Flow<IntentSender?> {
        return _currentAuthState
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

    private suspend fun updateUserStore(uid: String) {
        dataStore.updateIsFirstTime(uid)
    }

    override suspend fun getCurrentUser(): UserInfo? {
        val getCurrentUserUid = dataStore.getUserUidFlow().first()
        val getUserInfo = userDao.getUserByUid(getCurrentUserUid)
        return getUserInfo?.toUserInfo()
    }

    override fun isUserAvailable() =
        combine(_currentUserState, userDao.getUsersFlow()) { userUid, listUsers ->
            val hasUid = userUid.isEmpty()
            if (hasUid) {
                false
            } else {
                val getCurrentUser = listUsers.find { it.uidUser == userUid }
                getCurrentUser != null
            }
        }
}