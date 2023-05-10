package com.swalif.sa.repository.userRepository

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.swalif.sa.core.data_store.getUserUidFlow
import com.swalif.sa.core.data_store.updateIsFirstTime
import com.swalif.sa.datasource.local.dao.UserDao
import com.swalif.sa.datasource.local.entity.UserEntity
import com.swalif.sa.datasource.remote.FireStoreDatabase
import com.swalif.sa.mapper.toUserDto
import com.swalif.sa.mapper.toUserEntity
import com.swalif.sa.mapper.toUserInfo
import com.swalif.sa.model.SignInResult
import com.swalif.sa.model.UserData
import com.swalif.sa.model.UserInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import logcat.logcat
import java.io.Closeable
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val dataStore: DataStore<Preferences>,
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context,
    private val fireStore: FireStoreDatabase
) : UserRepository, FirebaseAuth.AuthStateListener, Closeable {

    private val oneTapClint = Identity.getSignInClient(context)
    private val signInRequest = BeginSignInRequest.Builder().setGoogleIdTokenRequestOptions(
        BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
            .setServerClientId("123801215248-c2463dk439ifv2d3uq25ppgmpj7k4a4v.apps.googleusercontent.com")
            .setFilterByAuthorizedAccounts(true).build()
    ).setAutoSelectEnabled(true).build()

    override fun onAuthStateChanged(p0: FirebaseAuth) {
        val currentUser = p0.currentUser
        logcat {
            "called with user : $currentUser"
        }
    }

    override suspend fun signOut(uid: String) {
        oneTapClint.signOut().await()
        firebaseAuth.signOut()
        val getUserByUid = getUserByUid(uid)?:return
        deleteUser(getUserByUid)
    }

    override suspend fun getSignInResult(intent: Intent): SignInResult {
        val credential = oneTapClint.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val authResult = firebaseAuth.signInWithCredential(googleCredential).await()
            val user = authResult.user
            SignInResult(
                userData = user?.run {
                    UserData(
                        displayName,
                        uid,
                        photoUrl.toString(),
                        email,
                        authResult.additionalUserInfo?.isNewUser ?: false
                    )
                }, null
            )
        } catch (e: Exception) {
            SignInResult(
                null, e.message
            )
        }
    }

    override suspend fun signIn(): IntentSender? {
        return try {
            val result = oneTapClint.beginSignIn(signInRequest).await()
            result.pendingIntent.intentSender
        } catch (e: Exception) {
            logcat {
                "error ${e.message}"
            }
            null
        }
    }

    init {
        firebaseAuth.addAuthStateListener(this)
    }

    override fun close() {
        firebaseAuth.removeAuthStateListener(this)
    }



    override suspend fun saveUser(user: UserInfo) {
        val savedUser = fireStore.saveUserFirstTime(user.toUserDto())
        if (savedUser){
            insertUser(user)
            updateUserStore(user.uidUser)
        }
    }

    override suspend fun insertUser(user: UserInfo) {
        userDao.insertUser(user.toUserEntity())
        updateUserStore(user.uidUser)
    }

    override suspend fun deleteUser(user: UserInfo) {
        userDao.deleteUser(user.toUserEntity())
        updateUserStore("")
    }

    override suspend fun getUserByUid(uid: String): UserInfo? {
        return userDao.getUserByUid(uid)?.toUserInfo() ?: fireStore.getUserByUid(uid)?.toUserInfo()
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
        combine(dataStore.getUserUidFlow(), userDao.getUsersFlow()) { userUid, listUsers ->
            val hasUid = userUid.isEmpty()
            if (hasUid) {
                false
            } else {
                val getCurrentUser = listUsers.find { it.uidUser == userUid }
                getCurrentUser != null
            }
        }
}