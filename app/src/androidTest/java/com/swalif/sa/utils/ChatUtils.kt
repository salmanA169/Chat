package com.swalif.sa.utils

import com.swalif.sa.datasource.local.entity.ChatEntity
import com.swalif.sa.datasource.local.entity.MessageEntity
import com.swalif.sa.model.Chat
import com.swalif.sa.model.Message
import com.swalif.sa.model.MessageStatus
import com.swalif.sa.model.MessageType
import java.time.LocalDateTime
import kotlin.random.Random

private const val MYUID = "test"
private val fakeChatsIDs = mapOf(1 to "salman", 2 to "saleh")

val chatList = listOf(
    Chat(1, "test", "test", "salman", "Hi", "", LocalDateTime.now(), 0, "test"),
    Chat(2, "", "", "saleh", "", "", LocalDateTime.now(), 0, "")
)

val messagesList = listOf(
    Message(0,2,"test","", LocalDateTime.now(),null,MessageStatus.SEEN,MessageType.TEXT),
    Message(0,2,"test","", LocalDateTime.now(),null,MessageStatus.SEEN,MessageType.TEXT),
    Message(0,1,"test","", LocalDateTime.now(),null,MessageStatus.SEEN,MessageType.TEXT),
    Message(0,3,"test","", LocalDateTime.now(),null,MessageStatus.SEEN,MessageType.TEXT),
    Message(0,3,"test","", LocalDateTime.now(),null,MessageStatus.SEEN,MessageType.TEXT),
)