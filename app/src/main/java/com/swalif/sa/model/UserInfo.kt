package com.swalif.sa.model

import com.swalif.sa.component.Gender

data class UserInfo(
    val uidUser:String,
    val userName:String,
    val email:String,
    val gender:Gender,
    val uniqueId:String,
    val createdAt:Long,
    val photoUri:String
)
