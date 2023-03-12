package com.swalif.sa.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.swalif.sa.datasource.local.convertor.Convertor
import com.swalif.sa.datasource.local.dao.ChatDao
import com.swalif.sa.datasource.local.dao.MessageDao
import com.swalif.sa.datasource.local.dao.UserDao
import com.swalif.sa.datasource.local.entity.ChatEntity
import com.swalif.sa.datasource.local.entity.MessageEntity
import com.swalif.sa.datasource.local.entity.UserEntity

@Database(
    version = 1,
    entities = [UserEntity::class, ChatEntity::class, MessageEntity::class],

    )
@TypeConverters(value = [Convertor::class])
abstract class SwalifDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val chatDao: ChatDao
    abstract val messageDao: MessageDao
}