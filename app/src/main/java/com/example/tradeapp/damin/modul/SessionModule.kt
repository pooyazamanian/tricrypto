package com.example.tradeapp.damin.modul

import com.example.tradeapp.damin.repository.AuthenticationRepository
import com.example.tradeapp.damin.session.SessionManager
import io.github.jan.supabase.auth.Auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionModule {

    @Provides
    @Singleton
    fun provideSessionManager(
        auth: Auth,
        authRepository: AuthenticationRepository
    ): SessionManager {
        return SessionManager(auth, authRepository)
    }
}