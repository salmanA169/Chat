package com.swalif.sa.mapper

import com.google.firebase.Timestamp
import com.swalif.sa.datasource.local.entity.UserEntity
import com.swalif.sa.datasource.remote.firestore_dto.UserDto
import com.swalif.sa.model.UserInfo
import java.util.Date



fun UserInfo.toUserEntity() = UserEntity(
    uidUser, userName, email, gender, uniqueId, createdAt, photoUri
)

fun UserInfo.toUserDto() = UserDto(
    uidUser, userName, email, gender, uniqueId, Timestamp(Date(createdAt)),photoUri
)

fun UserEntity.toUserInfo() = UserInfo(
    uidUser, userName, email, gender, uniqueId, createdAt, photoUri
)
