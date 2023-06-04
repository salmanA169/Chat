package com.swalif.sa.features.main.home

import androidx.compose.runtime.Immutable
import com.swalif.sa.model.Chat

@Immutable
data class HomeChatState(
    val chats:List<Chat> = emptyList(),
    val tempChats :List<Chat> = emptyList(),
    val myUid:String? = null
)
