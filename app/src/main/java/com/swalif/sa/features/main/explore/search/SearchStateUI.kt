package com.swalif.sa.features.main.explore.search

import androidx.compose.runtime.Immutable
import com.swalif.sa.model.UserInfo

@Immutable
data class SearchStateUI(
    val userInfo: UserInfo? = null,
    val searchStateResult : SearchStateResult = SearchStateResult.SEARCHING
){
}

enum class SearchStateResult{
    SEARCHING,FOUND_USER,USER_LEFT,USER_ACCEPT;
    fun isUserLeft() = this == SearchStateResult.USER_LEFT

}