package com.swalif.sa.mapper

import com.swalif.sa.datasource.local.entity.UserEntity
import com.swalif.sa.model.LoginInfo


fun UserEntity.toUserInfo() = LoginInfo(
    uidUser,userName,email,photoUri
)
