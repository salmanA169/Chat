package com.swalif.sa.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.swalif.sa.R
import com.swalif.sa.utils.formatSortTime

data class ChatInfo(
    val userName: String = "",
    val userStatus: UserStatus? = null,
    val uidUser: String = "",
    val imageUri: String = "",
    val userIsLeft:Boolean = false,
    val requestFriendStatus:RequestFriendStatus = RequestFriendStatus.IDLE
) {
    @Composable
    fun localizeStatusUser(): String {
        return when (userStatus) {
            is UserStatus.Offline -> stringResource(
                id = R.string.last_seen,
                userStatus.lastSeen.formatSortTime()
            )
            UserStatus.Online -> {
                stringResource(id = R.string.online)
            }
            UserStatus.TYPING -> stringResource(id = R.string.typing)
            null -> ""
        }

    }
}
enum class RequestFriendStatus{
    IDLE,SENT,ACCEPTED
}
sealed class UserStatus {
    object Online : UserStatus()
    class Offline(val lastSeen: Long) : UserStatus()
    object TYPING : UserStatus()

}
