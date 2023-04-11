package com.swalif.sa.di

import com.swalif.sa.coroutine.DispatcherProvider
import com.swalif.sa.coroutine.DispatcherProviderImpl
import com.swalif.sa.fakeRepo.ChatFakeRepo
import com.swalif.sa.fakeRepo.FakeMessageRepository
import com.swalif.sa.fakeRepo.FakeUserRepository
import com.swalif.sa.fakeRepo.TestDispatcherCoroutine
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
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class RepoModuleTest {

    @Binds
    @Singleton
    abstract fun bindFakeRepo(
        userRepository: UserRepositoryImpl
    ):UserRepository

    @Binds
    @Singleton
    abstract fun  bindFakeChatRepo(
    chatRepository: ChatRepositoryImpl
    ):ChatRepository

    @Binds
    @Singleton
    abstract fun bindFakeMessage(
    messageRepository: MessageRepositoryImpl
    ):MessageRepository

    @Singleton
    @Binds
    abstract fun providerDispatcherProvider(
        dispatcherProvider: TestDispatcherCoroutine
    ): DispatcherProvider
}