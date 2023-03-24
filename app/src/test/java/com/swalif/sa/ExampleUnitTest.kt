package com.swalif.sa

import com.google.common.truth.Truth
import com.swalif.sa.model.Message
import com.swalif.sa.model.MessageStatus
import com.swalif.sa.model.MessageType
import org.junit.Test

import org.junit.Assert.*
import java.time.LocalDateTime

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun checkMessageMedia_ReturnTrue() {
        val imageMessage = Message(
            0,
            0,
            "",
            "",
            LocalDateTime.now(),
            "salman",
            MessageStatus.SEEN,
            MessageType.IMAGE
        )
        val textMessage =
            Message(0, 0, "", "hi", LocalDateTime.now(), null, MessageStatus.SEEN, MessageType.TEXT)

    }
}