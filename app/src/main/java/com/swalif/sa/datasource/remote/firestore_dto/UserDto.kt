package com.swalif.sa.datasource.remote.firestore_dto

import com.google.firebase.Timestamp
import com.swalif.sa.component.Gender
import com.swalif.sa.model.UserStatus

data class UserDto(
    val uidUser: String = "",
    val userName: String = "",
    val email: String = "",
    val gender: Gender? = null,
    val uniqueId: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val photoUri: String = "",
    val userStatus: UserStatusDto = UserStatusDto.OFFLINE,
    val lastSeen: Timestamp? = null
)

enum class UserStatusDto {
    ONLINE, OFFLINE, TYPING
}

fun UserDto.localizeToUserStatus(): UserStatus {
    return when (this.userStatus) {
        UserStatusDto.ONLINE -> UserStatus.Online
        UserStatusDto.OFFLINE -> UserStatus.Offline(
            lastSeen?.toDate()?.time?:0
        )

        UserStatusDto.TYPING -> UserStatus.TYPING
    }
}