package com.swalif.sa.di

import com.swalif.sa.fakeRepo.FakeFireStoreChatWithMessages
import com.swalif.sa.fakeRepo.FakeSearchRepo
import com.swalif.sa.repository.firestoreChatMessagesRepo.FirestoreChatMessageRepository
import com.swalif.sa.repository.firestoreChatMessagesRepo.FirestoreChatWithMessageRepositoryImpl
import com.swalif.sa.repository.searchRepository.SearchRepository
import com.swalif.sa.repository.searchRepository.SearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = arrayOf(ViewModelComponent::class),
    replaces = [DiModuleViewModelScope::class]
)
abstract class DiModuleTestViewModelScope {

    @ViewModelScoped
    @Binds
    abstract fun provideSearchRepository(
        searchRepository: FakeSearchRepo
    ): SearchRepository

    @ViewModelScoped
    @Binds
    abstract fun provideFirestoreRepo(
        firestoreChatWithMessageRepositoryImpl: FakeFireStoreChatWithMessages
    ): FirestoreChatMessageRepository
}