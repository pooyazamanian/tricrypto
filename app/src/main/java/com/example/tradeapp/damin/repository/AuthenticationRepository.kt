package com.example.tradeapp.damin.repository

import com.example.tradeapp.damin.util.Result
import io.github.jan.supabase.auth.user.UserInfo

interface AuthenticationRepository {
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    fun getCurrentUser(): UserInfo?
}