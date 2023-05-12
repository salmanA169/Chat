package com.swalif.sa.datasource.remote.firestore_dto

import com.swalif.sa.core.searchManager.RoomStatus
import com.swalif.sa.core.searchManager.UserState
import com.swalif.sa.core.searchManager.Users
import com.swalif.sa.mapper.toUserInfo

data class RoomResultDto(
    val users : List<UsersDto> = emptyList(),
    val maxUsers:Int = 0,
    val roomStatus: RoomStatus = RoomStatus.WAITING_USERS,
    val roomId:String =""
)

data class UsersDto(
    val user:UserDto = UserDto(),
    val userState: UserState = UserState.IDLE
)

fun UsersDto.toUsers() = Users(user.toUserInfo(),userState)

fun List<UsersDto>.toUsers() = map { it.toUsers() }