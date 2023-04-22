package com.swalif.sa.repository.searchRepository

import com.swalif.sa.features.main.explore.search.SearchStateResult
import com.swalif.sa.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun searchUser(): Flow<SearchInfo>
    suspend fun ignoreUser()
    suspend fun acceptUser()
}

data class SearchInfo(
    val userInfo: UserInfo,
    val searchStateResult: SearchStateResult
)
