package com.swalif.sa.di

import android.content.Context
import androidx.room.Room
import com.swalif.sa.datasource.local.SwalifDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DiModule::class]
)
object DiModuleTest {

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

