package com.swalif.sa.datasource.local.dao

import androidx.room.*
import com.swalif.sa.datasource.local.entity.MessageEntity
import com.swalif.sa.datasource.local.relation.ChatWithMessages
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Transaction
    @Query("SELECT * FROM ChatEntity WHERE chatId =:chatId")
    fun getMessageWithChat(chatId:String):Flow<ChatWithMessages>

    @Query("SELECT * FROM MessageEntity ")
    fun observeAllMessages():Flow<MessageEntity>

    @Query("SELECT * FROM MessageEntity")
    suspend fun getAllMessages():List<MessageEntity>

    @Query("SELECT * FROM MessageEntity WHERE chatIdMessage = :chatId")
    suspend fun getMessageByChatId(chatId: String):List<MessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMessage(messageEntity: MessageEntity):Long

    @Update
    suspend fun updateMessage(messageEntity: MessageEntity)

    @Delete
    suspend fun deleteMessage(messageEntity: MessageEntity)
}
