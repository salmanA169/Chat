package com.swalif.sa.di

import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.coroutine.DispatcherProviderImpl
import com.swalif.sa.repository.chatRepositoy.ChatRepository
import com.swalif.sa.repository.chatRepositoy.ChatRepositoryImpl
import com.swalif.sa.repository.messageRepository.MessageRepository
import com.swalif.sa.repository.messageRepository.MessageRepositoryImpl
import com.swalif.sa.repository.userRepository.UserRepository
import com.swalif.sa.repository.userRepository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun provideUserRepo(
        userRepository: UserRepositoryImpl
    ): UserRepository

    @Singleton
    @Binds
    abstract fun provideMessageRepo(
        messageRepository: MessageRepositoryImpl
    ): MessageRepository

    @Singleton
    @Binds
    abstract fun provideChatRepo(
        chatRepository: ChatRepositoryImpl
    ): ChatRepository

    @Singleton
    @Binds
    abstract fun providerDispatcherProvider(
        dispatcherProvider: DispatcherProviderImpl
    ):DispatcherProvider
}