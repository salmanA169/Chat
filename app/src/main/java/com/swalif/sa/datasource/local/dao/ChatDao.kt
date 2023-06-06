package com.swalif.sa.datasource.local.dao

import androidx.room.*
import com.swalif.sa.datasource.local.entity.ChatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT  * FROM chatentity")
    fun getChats():Flow<List<ChatEntity>>

    @Query("SELECT * FROM chatentity WHERE chatId = :chatId")
    suspend fun getChatById(chatId:String):ChatEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chatEntity: ChatEntity)

    @Update
    suspend fun updateChat(chatEntity: ChatEntity)

    @Delete
    suspend fun deleteChatById(chat: ChatEntity)

    @Query("DELETE FROM chatentity")
    suspend fun nukeChatTable()
}