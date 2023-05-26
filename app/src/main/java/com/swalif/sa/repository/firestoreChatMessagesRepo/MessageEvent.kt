package com.swalif.sa.repository.firestoreChatMessagesRepo

interface MessageEvent {
    fun onDataChanged()
    fun onFriendAccepted()
}