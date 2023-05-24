package com.swalif.sa

import android.content.Intent
import android.content.IntentSender
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.swalif.sa.features.main.home.HomeViewModel
import com.swalif.sa.model.Chat
import com.swalif.sa.model.SignInResult
import com.swalif.sa.model.UserInfo
import com.swalif.sa.repo.FakeChatRepo
import com.swalif.sa.repo.MessageFakeRepository
import com.swalif.sa.repo.TestDispatcherProvider
import com.swalif.sa.repository.userRepository.UserRepository
import com.swalif.sa.utils.MainDispatcherRule
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import java.time.LocalDateTime

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class HomeViewModelTest {
    private lateinit var homeViewModel: HomeViewModel

    @get:Rule
    val dispatcherRule = MainDispatcherRule()
    @Before
    fun setup() {
        val messageRepo = MessageFakeRepository()
        val chatRepo = FakeChatRepo()
        val testDispatcher = TestDispatcherProvider()

        homeViewModel = HomeViewModel(chatRepo, messageRepo, testDispatcher,object : UserRepository{
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

            override fun isUserAvailable(): Flow<Boolean> {
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
        })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addChat_returnTrue() = runTest {
//        val chat =
//            Chat(
//                1,
//                "sender test",
//                "test",
//                "salman",
//                "Hi",
//                "",
//                LocalDateTime.now(),
//                1,
//                "hi"
//            )
//       backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
//            homeViewModel.homeState.collect{
//                println(it)
//            }
//        }
//        homeViewModel.homeState.test {
//            Truth.assertThat(awaitItem().chats).isEmpty()
//            homeViewModel.addTestChat(chat)
//            val s = awaitItem().chats
//            Truth.assertThat(s).containsExactly(chat)
//            println(s)
//        }

    }
}