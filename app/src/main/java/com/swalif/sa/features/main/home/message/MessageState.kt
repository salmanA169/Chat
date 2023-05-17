package com.swalif.sa.features.main.home.message

import androidx.compose.runtime.Immutable
import com.swalif.sa.model.ChatInfo
import com.swalif.sa.model.Message


@Immutable
data class MessageState(
    val messages : List<Message> = emptyList(),
    val text:String = "",
    val chatInfo:ChatInfo = ChatInfo(),
    val myUid:String = " "
)