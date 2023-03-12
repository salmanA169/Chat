package com.swalif.sa

import com.google.common.truth.Truth
import com.swalif.sa.datasource.local.entity.UserEntity
import com.swalif.sa.repository.userRepository.UserRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@HiltAndroidTest
class DatabaseTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userRepository: UserRepository

    @Before
    fun before() {
        hiltRule.inject()
    }

    @Test
    fun insertUserAndGet_Correct() {
        runBlocking {
            val fakeUser = UserEntity(
                "fakeUid",
                "salman", "salman.alamoudi95@gmail.com", LocalDateTime.now().toEpochSecond(
                    ZoneOffset.UTC),
                ""
            )
            userRepository.insertUser(fakeUser)
            val getUserbyUid = userRepository.getUserByUid("fakeUid")!!
            Truth.assertThat(getUserbyUid.username).isEqualTo("salman")
            Truth.assertThat(getUserbyUid.email).isEqualTo("salman.alamoudi95@gmail.com")
        }
    }

}