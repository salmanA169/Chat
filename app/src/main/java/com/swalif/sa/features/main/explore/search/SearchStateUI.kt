package com.swalif.sa.features.main.explore.search

import androidx.compose.runtime.Immutable
import com.swalif.sa.core.searchManager.RoomEvent
import com.swalif.sa.model.UserInfo

@Immutable
data class SearchStateUI(
    val roomEvent :RoomEvent = RoomEvent(),
    val myCurrentUser:UserInfo? = null,

)
