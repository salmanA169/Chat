package com.swalif.sa.repository.messageRepository

import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.core.net.toUri
import com.swalif.sa.R
import com.swalif.sa.core.storage.FilesManager
import com.swalif.sa.datasource.local.dao.ChatDao
import com.swalif.sa.datasource.local.dao.MessageDao
import com.swalif.sa.datasource.local.relation.ChatWithMessages
import com.swalif.sa.mapper.toMessageEntity
import com.swalif.sa.mapper.toMessageList
import com.swalif.sa.model.Message
import com.swalif.sa.model.MessageType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import java.io.File
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val messageDao: MessageDao,
    private val filesManager: FilesManager
) : MessageRepository {
    // TODO: play media in notification volume and improve it
//    private val mediaPlayer = MediaPlayer.create(context, R.raw.google_notification,AudioAttributes.Builder()
//        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//        .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
//        .build(),1).apply {
//
//    }

    override suspend fun addMessage(message: Message) {
        when (message.messageType) {
            MessageType.TEXT -> {
                messageDao.addMessage(message.toMessageEntity())
            }
            MessageType.IMAGE -> {
                val id = messageDao.addMessage(message.copy(mediaUri = "").toMessageEntity())
                val uriImage = message.mediaUri!!
                val nameFile = UUID.randomUUID().toString().plus(".jpeg")
                val rootDir = File(context.filesDir, nameFile).toUri().toString()
                if (filesManager.saveImage(uriImage.toUri(), nameFile)) {
                    messageDao.updateMessage(
                        message.copy(messageId = id.toInt(), mediaUri = rootDir).toMessageEntity()
                    )
                }
            }
            MessageType.AUDIO -> TODO()
        }
//        mediaPlayer.start()
    }


    override suspend fun getMessages(): List<Message> {
        return messageDao.getMessages().toMessageList()
    }

    override fun getMessages(chatId: Int): Flow<ChatWithMessages> {
        return messageDao.getMessage(chatId)
    }

    override suspend fun updateMessage(message: Message) {
        messageDao.updateMessage(message.toMessageEntity())
    }

    override suspend fun deleteMessages(message: List<Message>) {
        message.forEach {
            messageDao.deleteMessage(it.toMessageEntity())
        }
    }
}