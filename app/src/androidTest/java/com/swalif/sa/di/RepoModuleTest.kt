package com.swalif.sa.di

import com.swalif.sa.fakeRepo.ChatFakeRepo
import com.swalif.sa.fakeRepo.FakeMessageRepository
import com.swalif.sa.fakeRepo.FakeUserRepository
import com.swalif.sa.repository.chatRepositoy.ChatRepository
import com.swalif.sa.repository.chatRepositoy.ChatRepositoryImpl
import com.swalif.sa.repository.messageRepository.MessageRepository
import com.swalif.sa.repository.userRepository.UserRepository
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
        userRepository: FakeUserRepository
    ):UserRepository

    @Binds
    @Singleton
    abstract fun  bindFakeChatRepo(
    chatRepository: ChatFakeRepo
    ):ChatRepository

    @Binds
    @Singleton
    abstract fun bindFakeMessage(
    messageRepository: FakeMessageRepository
    ):MessageRepository
}