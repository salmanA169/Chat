package com.swalif.sa.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey
    val uidUser:String,
    val userName:String,
    val email:String,
    val gender:String,
    val createdAt:Long,
    val photoUri:String
)
