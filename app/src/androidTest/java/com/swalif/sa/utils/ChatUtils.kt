package com.swalif.sa.utils

import com.swalif.sa.datasource.local.entity.ChatEntity
import com.swalif.sa.datasource.local.entity.MessageEntity
import com.swalif.sa.model.MessageStatus
import java.time.LocalDateTime
import kotlin.random.Random

private const val MYUID = "test"
private val fakeChatsIDs = mapOf(1 to "salman", 2 to "saleh")
fun fakeChatAndMessage(): FakeChatsAndMessages {
    val chats = generateChats()
    val messages = generateMessages()
    return FakeChatsAndMessages(chats,messages)
}
fun generateMessages() = buildList<MessageEntity> {
    for (i in 1 until 20) {
        fakeChatsIDs.forEach { id, uidUser ->
            add(
                MessageEntity(
                    0, id, uidUser, "message 1 - $i", LocalDateTime.now(),MessageStatus.SEEN
                )
            )
        }
    }
}

fun generateChats() = buildList<ChatEntity> {
    fakeChatsIDs.forEach { id, uidUser ->
        add(
            ChatEntity(
                id, uidUser, uidUser, MYUID, uidUser.toString(), "", LocalDateTime.now(), 1
            )
        )
    }
}

data class FakeChatsAndMessages(val chats: List<ChatEntity>, val message: List<MessageEntity>)