package com.swalif.sa.repository.searchRepository

import com.swalif.sa.repository.chatRepositoy.ChatRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val chatRepository:ChatRepository
):SearchRepository {

    override fun searchUser(): Flow<SearchInfo> {
        return callbackFlow{


            awaitClose {

            }
        }
    }

    override suspend fun ignoreUser() {
        TODO("Not yet implemented")
    }

    override suspend fun acceptUser() {
        TODO("Not yet implemented")
    }
}