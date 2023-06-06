package com.swalif.sa

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.swalif.sa.datasource.local.SwalifDatabase
import com.swalif.sa.datasource.local.entity.ChatEntity
import com.swalif.sa.datasource.local.entity.MessageEntity
import com.swalif.sa.model.MessageStatus
import com.swalif.sa.model.MessageType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var database: SwalifDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            SwalifDatabase::class.java
        ).build()
    }

    @Test
    fun useAppContext() {
        runTest {
            val chat = (1..5).map{
                MessageEntity(it,it.toString(),"","",0,"",MessageStatus.SENT,MessageType.TEXT,)
            }.forEach {
                database.messageDao.addMessage(it)
            }

            database.messageDao.observeAllMessages().test {
                Truth.assertThat(awaitItem()).hasSize(5)
                database.messageDao.nukeAllMessageTable()
                Truth.assertThat(awaitItem()).hasSize(0)
            }

        }
    }
}