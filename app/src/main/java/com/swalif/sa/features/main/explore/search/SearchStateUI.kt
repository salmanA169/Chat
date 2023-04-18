package com.swalif.sa.features.main.explore.search

import androidx.compose.runtime.Immutable
import com.swalif.sa.model.UserInfo

@Immutable
data class SearchStateUI(
    val userInfo: UserInfo? = null,
    val isLeft : Boolean = false
)