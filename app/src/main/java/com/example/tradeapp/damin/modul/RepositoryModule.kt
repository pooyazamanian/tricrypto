package com.example.tradeapp.damin.modul

import com.example.tradeapp.repository.ProfileRepositoryImp
import com.example.tradeapp.damin.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProfileRepository(
        impl: ProfileRepositoryImp
    ): ProfileRepository = impl
}
