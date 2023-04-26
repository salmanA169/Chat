package com.swalif.sa.core.searchManager

import com.swalif.sa.component.Gender
import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.model.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject
import kotlin.random.Random

private val fakeData = listOf<UserInfo>(
    UserInfo(
        "salman",
        "1",
        "https://play-lh.googleusercontent.com/HHJb4ew7S16SHjqNjp1nEkVKn8L2j1rXPjVmF4fqf-mGjZYYIjhHYKjUJSLbB7SRx1HS",
        Gender.MALE,
        0
    ),
    UserInfo(
        "saleh",
        "5",
        "https://static.vecteezy.com/system/resources/previews/011/484/429/original/anime-boy-avatar-vector.jpg",
        Gender.MALE,
        0
    ),
    UserInfo(
        "Ahmed",
        "2",
        "https://static.vecteezy.com/system/resources/previews/011/484/429/original/anime-boy-avatar-vector.jpg",
        Gender.FEMALE,0
    ),
    UserInfo(
        "salma",
        "3",
        "data:image/jpeg;base64,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",
        Gender.FEMALE,0
    ),
    UserInfo(
        "Mariam",
        "4",
        "https://i.pinimg.com/750x/ef/1e/4f/ef1e4f5363d81316cdc5f71ad8a1d34b.jpg",
        Gender.FEMALE,0
    )
)
class SearchManagerFakeData @Inject constructor(
    dispatcherProvider: DispatcherProvider
) : SearchManager {
    private var job: Job? = null
    private val scope = CoroutineScope(dispatcherProvider.default)
    override var onSearchEventListener: SearchEvent? = null
    private var currentRoom: AbstractChatRoom? = null
    private var currentUserInfo:UserInfo? = null
    private val fakeChatRooms = listOf<AbstractChatRoom>(
        FakeRandomChatRoom()
    )

    override fun registerSearchEvent(userInfo: UserInfo) {
        job?.cancel()
        job = scope.launch {
            val randomRoom = fakeChatRooms.random()
            currentRoom = randomRoom
            randomRoom.addUser(userInfo)
            currentUserInfo = userInfo
            launch {
                delay(5000)
                val randomUser = fakeData.random()
                currentRoom!!.addUser(
                    randomUser
                )
                delay(3000)
                currentRoom!!.updateUserStatus(randomUser.userUid,if (Random.nextBoolean()) UserState.IGNORE else UserState.ACCEPT)
            }
            randomRoom.roomEvent.collect {
                logcat("SearchManager") { it.toString() }
                onSearchEventListener?.onEvent(it)
            }

        }

    }

    override fun updateUserStatus(userState: UserState) {
        currentRoom!!.updateUserStatus(currentUserInfo!!.userUid, userState)
        if (userState == UserState.IGNORE){
            // search Another User
            val findUserUidFromRoom = currentRoom!!.getUIDUsersInRoom().find { it != currentUserInfo!!.userUid }
            val getUser =fakeData.find{ it.userUid == findUserUidFromRoom }
            currentRoom!!.removeUser(getUser!!)
            currentRoom!!.removeUser(currentUserInfo!!)
            reload()
        }
    }


    override fun reload() {
        job?.cancel()
        job = null
        registerSearchEvent(currentUserInfo!!)
    }

    override fun deleteRoom(roomId: String) {
        TODO("Not yet implemented")
    }

    override fun unregisterSearchEvent() {
        job?.cancel()
        job = null
        currentRoom = null
        currentUserInfo = null
    }

    override fun close() {
        logcat {
            "called close SearchManagerFakeData"
        }
        unregisterSearchEvent()
    }
}