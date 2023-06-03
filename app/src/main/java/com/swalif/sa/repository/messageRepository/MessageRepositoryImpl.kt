package com.swalif.sa.repository.messageRepository

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.swalif.sa.core.storage.FilesManager
import com.swalif.sa.datasource.local.dao.MessageDao
import com.swalif.sa.datasource.local.relation.ChatWithMessages
import com.swalif.sa.mapper.toMessageEntity
import com.swalif.sa.mapper.toMessageList
import com.swalif.sa.model.Message
import com.swalif.sa.model.MessageType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import logcat.logcat
import java.io.File
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
                val nameFile = uriImage.toUri().lastPathSegment!!.substringAfter("/")
                val file = File(context.filesDir,nameFile)

                val savedFile = filesManager.saveImage(uriImage,nameFile)
                if (savedFile) {
                    messageDao.updateMessage(
                        message.copy(messageId = id.toInt(), mediaUri = file.toUri().toString()).toMessageEntity()
                    )
                }
            }

            MessageType.AUDIO -> TODO()
            else -> {}
        }
//        mediaPlayer.start()
    }

    override suspend fun getMessagesByChatID(chatId: String): List<Message> {
        return messageDao.getMessageByChatId(chatId).toMessageList()
    }

    override suspend fun getAllMessages(): List<Message> {
        return messageDao.getAllMessages().toMessageList()
    }

    override fun observeMessageByChatId(chatId: String): Flow<ChatWithMessages> {
        return messageDao.getMessageWithChat(chatId)
    }

    override suspend fun updateMessage(message: Message) {
        var tempMessage = message
        if (message.messageType.isMedia()){
            val nameFile = tempMessage.mediaUri!!.toUri().lastPathSegment!!.substringAfter("/")
            if (!filesManager.ifFileAvailable(nameFile)){
                logcat { "called file not exist" }
                val savedFile = filesManager.saveImage(tempMessage.mediaUri!!,nameFile)
                val fileName = File(context.filesDir,nameFile)
                if (savedFile){
                    tempMessage = tempMessage.copy(mediaUri = fileName.toUri().toString())
                    messageDao.updateMessage(tempMessage.toMessageEntity())
                }
            }else{
                val getMessageById = getAllMessages().find { it.messageId == tempMessage.messageId }!!
                getMessageById.let {
                    messageDao.updateMessage(it.toMessageEntity().copy(statusMessage =message.statusMessage))
                }
            }
        }else{
            messageDao.updateMessage(tempMessage.toMessageEntity())
        }

    }

    override suspend fun deleteMessage(message: Message) {
        // TODO: delete file storage
        messageDao.deleteMessage(message.toMessageEntity())
    }

    override suspend fun deleteMessagesByChatId(chatId: String) {
        // TODO: also delete medial files
        val getMessage = messageDao.getMessageByChatId(chatId)
        deleteMessages(getMessage.toMessageList())
    }

    override suspend fun deleteMessages(message: List<Message>) {
        message.forEach {
            // TODO: also delete medial files
            messageDao.deleteMessage(it.toMessageEntity())
        }
    }
}