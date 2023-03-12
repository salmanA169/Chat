package com.swalif.sa.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.swalif.sa.R
import java.time.LocalDateTime

data class ChatInfo(
    val userName: String = "",
    val userStatus: UserStatus? = null,
    val uidUser:String = "",
    val imageUri:String = ""
){
    @Composable
    fun localizeStatusUser():String {
         return when(userStatus){
            is UserStatus.Offline -> ""// TODO: format date string
            UserStatus.Online -> {
                stringResource(id = R.string.online)
            }
            UserStatus.TYPING -> stringResource(id = R.string.typing)
            null -> ""
        }

    }
}

sealed class UserStatus {
    object Online : UserStatus()
    class Offline (val lastSeen:LocalDateTime): UserStatus()
    object TYPING:UserStatus()

    fun getNextStatus():UserStatus{
        return when(this){
            is Offline -> TYPING
            Online -> Offline(LocalDateTime.now())
            TYPING -> Online
        }
    }
}
