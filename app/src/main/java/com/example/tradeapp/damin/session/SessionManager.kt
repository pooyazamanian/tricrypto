package com.example.tradeapp.damin.session

import com.example.tradeapp.damin.repository.AuthenticationRepository
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val auth: Auth,
    private val authRepository: AuthenticationRepository
) {
    private val scope = CoroutineScope(Dispatchers.Main)

    val authState: StateFlow<AuthState> = auth.sessionStatus
        .map { status ->
            when (status) {
                is SessionStatus.Authenticated -> AuthState.Authenticated
                is SessionStatus.NotAuthenticated -> AuthState.Unauthenticated
                is SessionStatus.Initializing -> AuthState.Loading
                is SessionStatus.RefreshFailure -> AuthState.SessionExpired
            }
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AuthState.Loading
        )

    suspend fun logout() {
        authRepository.logout()
    }

    fun getCurrentUser() = authRepository.getCurrentUser()
}