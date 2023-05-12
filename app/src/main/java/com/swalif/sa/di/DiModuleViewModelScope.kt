package com.swalif.sa.di

import com.swalif.sa.core.searchManager.SearchManager
import com.swalif.sa.core.searchManager.SearchManagerFakeData
import com.swalif.sa.core.searchManager.SearchManagerFireStore
import com.swalif.sa.repository.searchRepository.SearchRepository
import com.swalif.sa.repository.searchRepository.SearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class DiModuleViewModelScope {

    @ViewModelScoped
    @Binds
    abstract fun provideSearchManager(
        searchManager: SearchManagerFireStore
    ): SearchManager

    @ViewModelScoped
    @Binds
    abstract fun provideSearchRepository(
        searchRepository: SearchRepositoryImpl
    ): SearchRepository

}