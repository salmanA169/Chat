package com.swalif.sa.model

import com.swalif.sa.component.Gender

data class UserInfo(
    val username:String,
    val userUid:String,
    val imageUri:String,
    val gender : Gender,
    val createAt:Long
)
