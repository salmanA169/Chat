package com.swalif.sa.di

import com.swalif.sa.fakeRepo.FakeUserRepository
import com.swalif.sa.repository.userRepository.UserRepository
import dagger.Binds
import dagger.Module
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


}