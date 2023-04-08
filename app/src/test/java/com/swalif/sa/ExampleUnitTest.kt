package com.swalif.sa

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.swalif.sa.features.main.home.HomeViewModel
import com.swalif.sa.model.Chat
import com.swalif.sa.model.Message
import com.swalif.sa.model.MessageStatus
import com.swalif.sa.model.MessageType
import com.swalif.sa.repo.FakeChatRepo
import com.swalif.sa.repo.MessageFakeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import java.time.LocalDateTime

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup() {
        val messageRepo = MessageFakeRepository()
        val chatRepo = FakeChatRepo()
        homeViewModel = HomeViewModel(chatRepo, messageRepo)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addChat_returnTrue() = runTest {
        val chat =
            Chat(1, "sender test", "test", "salman", "Hi", "", LocalDateTime.now(), 1, "hi")
        val s = MutableSharedFlow<Int>()
        val o = s.stateIn(this, SharingStarted.WhileSubscribed(5000), 0)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            o.collect()
        }

//            Truth.assertThat(homeViewModel.homeState.value.chats).isEmpty()
//            homeViewModel.addTestChat(chat)
//            Truth.assertThat(homeViewModel.homeState.value.chats).containsExactly(chat)
//            o.test {
//            }
        Truth.assertThat(o.value).isEqualTo(0)
        s.emit(5)
        Truth.assertThat(o.value).isEqualTo(5)


    }
}