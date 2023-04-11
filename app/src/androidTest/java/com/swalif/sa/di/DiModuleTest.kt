package com.swalif.sa.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.swalif.sa.datasource.local.SwalifDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.createTestCoroutineScope
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DiModule::class]
)
object DiModuleTest {

    @Provides
    @Singleton
    fun provideDataStoreTest(@ApplicationContext context: Context)= PreferenceDataStoreFactory.create(
        scope = TestScope(UnconfinedTestDispatcher()),
        produceFile = { context.preferencesDataStoreFile("test") }
    )

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): SwalifDatabase {
        return Room.inMemoryDatabaseBuilder(context, SwalifDatabase::class.java).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: SwalifDatabase) = database.userDao

    @Provides
    @Singleton
    fun provideChatDao(database: SwalifDatabase) = database.chatDao

    @Provides
    @Singleton
    fun provideMessageDao(database: SwalifDatabase) = database.messageDao

}

