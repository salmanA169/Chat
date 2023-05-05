package com.swalif.sa.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.swalif.sa.component.Gender

@Entity
data class UserEntity(
    @PrimaryKey
    val uidUser:String,
    val userName:String,
    val email:String,
    val gender:Gender,
    val uniqueId:String,
    val createdAt:Long,
    val photoUri:String
)
