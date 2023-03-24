package com.swalif.sa

import com.google.common.truth.Truth
import com.google.common.truth.Truth.*
import com.swalif.sa.datasource.local.dao.ChatDao
import com.swalif.sa.datasource.local.dao.MessageDao
import com.swalif.sa.datasource.local.entity.ChatEntity
import com.swalif.sa.datasource.local.entity.MessageEntity
import com.swalif.sa.datasource.local.entity.UserEntity
import com.swalif.sa.di.DiModule
import com.swalif.sa.di.RepositoryModule
import com.swalif.sa.model.MessageStatus
import com.swalif.sa.repository.messageRepository.MessageRepository
import com.swalif.sa.repository.userRepository.UserRepository
import com.swalif.sa.utils.fakeChatAndMessage
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@HiltAndroidTest
class DatabaseTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var chatDao: ChatDao

    @Inject
    lateinit var messageDao: MessageDao

    @Before
    fun before() {
        hiltRule.inject()

    }


    @Test
    fun addChatAndMessages_ChatIsNotEmpty() = runBlocking {
        val chatsWithMessages = fakeChatAndMessage()
        chatsWithMessages.message.forEach {
            messageDao.addMessage(it)
        }
        chatsWithMessages.chats.forEach {
            chatDao.insertChat(it)
        }

        val chats = chatDao.getChats().first().find { it.senderName == "salman" }!!
        val messageFromSalman = messageDao.getMessage(chats.chatId).first()
        assertThat(messageFromSalman.chat.senderName).isEqualTo("salman")
        assertThat(messageFromSalman.messages).isNotEmpty()

//        println(messageFromSalman.messages)
    }

    @Test
    fun addMessageAndCheckLastMessage_returnTrue() {
        runBlocking {
            val chats =
                (1..2).map { ChatEntity(it, "$it", "$it", "me", "salman", "", LocalDateTime.now(), 2) }
            val messages = (1..5).map {
                MessageEntity(
                    0,
                    1,
                    "it.uidSenderUser",
                    "salman $it",
                    LocalDateTime.now(),
                    MessageStatus.SEEN
                )
            }
            chats.forEach {
                chatDao.insertChat(it)
            }
            messages.forEach {
                messageDao.addMessage(it)
            }
            val chatWithMessage=  messageDao.getMessage(1).first()
            val lastMessage = chatWithMessage.messages.last()
            assertThat(lastMessage.message).isEqualTo("salman 5")
            assertThat(chatWithMessage.messages.size).isEqualTo(5)
        }
    }
}