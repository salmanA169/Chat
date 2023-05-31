package com.swalif.sa.fakeRepo

import com.swalif.sa.core.searchManager.RoomEvent
import com.swalif.sa.model.UserInfo
import com.swalif.sa.repository.searchRepository.SearchRepository
import kotlinx.coroutines.flow.Flow
import java.io.Closeable
import javax.inject.Inject

class FakeSearchRepo @Inject constructor():SearchRepository {
    override fun searchUser(userInfo: UserInfo): Flow<RoomEvent> {
        TODO("Not yet implemented")
    }

    override suspend fun ignoreUser() {
        TODO("Not yet implemented")
    }

    override suspend fun acceptUser() {
        TODO("Not yet implemented")
    }

    override fun getClosable(): Closeable {
        TODO("Not yet implemented")
    }
}