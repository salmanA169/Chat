package com.swalif.sa.repository.searchRepository

import com.swalif.sa.core.searchManager.RoomEvent
import com.swalif.sa.core.searchManager.SearchEvent
import com.swalif.sa.core.searchManager.SearchManager
import com.swalif.sa.core.searchManager.UserState
import com.swalif.sa.model.UserInfo
import com.swalif.sa.repository.chatRepositoy.ChatRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.Closeable
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchManager: SearchManager,
) : SearchRepository {

    override fun getClosable(): Closeable {
        return searchManager
    }

    override fun searchUser(userInfo: UserInfo): Flow<RoomEvent> {
        return callbackFlow {
            searchManager.addSearchEventListener(SearchEvent {
                trySend(it)
            })
            searchManager.registerSearchEvent(userInfo)
            awaitClose {
                searchManager.unregisterSearchEvent()
            }
        }
    }


    override suspend fun ignoreUser() {
        searchManager.updateUserStatus(UserState.IGNORE)
    }

    override suspend fun acceptUser() {
        searchManager.updateUserStatus(UserState.ACCEPT)

    }
}