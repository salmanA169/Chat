package com.swalif.sa.datasource.remote.firestore_dto

import com.google.firebase.Timestamp
import com.swalif.sa.component.Gender

data class UserDto(
    val uidUser:String ="",
    val userName:String ="",
    val email:String = "",
    val gender:Gender? = null,
    val uniqueId:String = "",
    val createdAt:Timestamp = Timestamp.now(),
    val photoUri:String= ""
)