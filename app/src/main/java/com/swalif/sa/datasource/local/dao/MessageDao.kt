package com.swalif.sa.datasource.local.dao

import androidx.room.*
import com.swalif.sa.datasource.local.entity.MessageEntity
import com.swalif.sa.datasource.local.relation.ChatWithMessages
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Transaction
    @Query("SELECT * FROM ChatEntity WHERE chatId =:chatId")
    fun getMessage(chatId:Int):Flow<ChatWithMessages>

    @Query("SELECT * FROM MessageEntity ")
    fun getMessage():Flow<MessageEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMessage(messageEntity: MessageEntity):Long

    @Update
    suspend fun updateMessage(messageEntity: MessageEntity)
}
