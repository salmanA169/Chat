package com.swalif.sa.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.swalif.Constants
import com.swalif.sa.core.data_store.dataStore
import com.swalif.sa.datasource.local.SwalifDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModule {
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context) = context.dataStore

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, SwalifDatabase::class.java, Constants.DATATBASE_NAME).build()

    @Provides
    @Singleton
    fun provideUserDao(database: SwalifDatabase) = database.userDao

    @Provides
    @Singleton
    fun provideChatDao(database: SwalifDatabase) = database.chatDao

    @Provides
    @Singleton
    fun provideMessageDao(database: SwalifDatabase) = database.messageDao

    @Provides
    @Singleton
    fun provideFirebaseAuth() = Firebase.auth


    @Provides
    @Singleton
    fun provideFireStore() = Firebase.firestore
}