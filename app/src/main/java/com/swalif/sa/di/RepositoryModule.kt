package com.swalif.sa.di

import com.swalif.sa.core.searchManager.SearchManager
import com.swalif.sa.core.searchManager.SearchManagerFakeData
import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.coroutine.DispatcherProviderImpl
import com.swalif.sa.repository.chatRepositoy.ChatRepository
import com.swalif.sa.repository.chatRepositoy.ChatRepositoryImpl
import com.swalif.sa.repository.messageRepository.MessageRepository
import com.swalif.sa.repository.messageRepository.MessageRepositoryImpl
import com.swalif.sa.repository.searchRepository.SearchRepository
import com.swalif.sa.repository.searchRepository.SearchRepositoryImpl
import com.swalif.sa.repository.userRepository.UserRepository
import com.swalif.sa.repository.userRepository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class RepositoryModule {

    @ActivityRetainedScoped
    @Binds
    abstract fun provideUserRepo(
        userRepository: UserRepositoryImpl
    ): UserRepository

    @ActivityRetainedScoped
    @Binds
    abstract fun provideMessageRepo(
        messageRepository: MessageRepositoryImpl
    ): MessageRepository

    @ActivityRetainedScoped
    @Binds
    abstract fun provideChatRepo(
        chatRepository: ChatRepositoryImpl
    ): ChatRepository

    @ActivityRetainedScoped
    @Binds
    abstract fun providerDispatcherProvider(
        dispatcherProvider: DispatcherProviderImpl
    ):DispatcherProvider



}