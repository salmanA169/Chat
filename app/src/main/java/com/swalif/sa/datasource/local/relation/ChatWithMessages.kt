package com.swalif.sa.datasource.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.swalif.sa.datasource.local.entity.ChatEntity
import com.swalif.sa.datasource.local.entity.MessageEntity

data class ChatWithMessages(
    @Embedded val chat :ChatEntity,
    @Relation(
        parentColumn = "chatId",
        entityColumn = "chatIdMessage"
    )
    val messages:List<MessageEntity>
)
