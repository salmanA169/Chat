package com.swalif.sa.mapper

import com.swalif.sa.datasource.local.entity.UserEntity
import com.swalif.sa.model.UserInfo



fun UserEntity.toUserInfo() = UserInfo(
    userName,uidUser,uniqueId,photoUri,gender,createdAt
)