package com.swalif.sa.repository.firestoreChatMessagesRepo

interface MessageEvent {
    fun onDataChanged()
    fun onFriendAccepted()
    fun onLeaveChat()
    fun onChatDeleted()
}