package com.swalif.sa.viewmodel

import app.cash.turbine.test
import app.cash.turbine.testIn
import com.google.common.truth.Truth
import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.features.main.home.HomeViewModel
import com.swalif.sa.model.Chat
import com.swalif.sa.repository.chatRepositoy.ChatRepository
import com.swalif.sa.repository.messageRepository.MessageRepository
import com.swalif.sa.utils.MainDispatcherRule
import com.swalif.sa.utils.chatList
import com.swalif.sa.utils.messagesList
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import java.time.LocalDateTime
import javax.inject.Inject

@HiltAndroidTest
class TestViewModel {

    val mainRule = MainDispatcherRule()

    val hiltTestRule = HiltAndroidRule(this)

    @get:Rule
    val rule = RuleChain
        .outerRule(hiltTestRule)
        .around(mainRule)

    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var chatRepository: ChatRepository

    @Inject
    lateinit var messageRepository: MessageRepository

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider


    @Before
    fun setup() {
        hiltTestRule.inject()
        homeViewModel = HomeViewModel(chatRepository, messageRepository, dispatcherProvider)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addChat_returnTrue() = runTest {
        val chat = (0..2).map {
            Chat(0, "", "", "test", "", "", LocalDateTime.now(), 0, "")
        }

        homeViewModel.homeState.test {

            Truth.assertThat(awaitItem().chats).isEmpty()
            chat.forEach {
                homeViewModel.addTestChat(it)
            }

            Truth.assertThat(awaitItem().chats[0].chatId).isEqualTo(1)
            Truth.assertThat(awaitItem().chats[1].chatId).isEqualTo(2)
        }

//        Truth.assertThat(getChats[0].chatId).isEqualTo(0)
//        Truth.assertThat(getChats[1].chatId).isEqualTo(1)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addChatsWithMessagesAndDeleteChatAndCheckMessageIsDeleted_returnTrue() {
        runTest {
            messagesList.forEach {
                messageRepository.addMessage(it)
            }
            chatList.forEach {
                chatRepository.insertChat(it)
            }
            val getChat = chatRepository.getChats().testIn(backgroundScope)
            val getMessage = messageRepository.observeMessageByChatId()
            Truth.assertThat(getChat.awaitItem().size).isEqualTo(2)
            Truth.assertThat(getMessage.find{it.chatId ==3 }).isNotNull()

            // remove message with deleted chat
            homeViewModel.checkChatIsDeletedMessages()
            val getMessage1 = messageRepository.observeMessageByChatId()
            Truth.assertThat(getMessage1.find { it.chatId ==3 }).isNull()
        }
    }
}