package com.swalif.sa.repository.messageRepository

interface MessageEvent {
    fun onSentMessage()
    fun onNewMessageReceived()
}