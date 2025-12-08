package com.example.tradeapp.usecase

import com.example.tradeapp.damin.repository.AuthenticationRepository
import javax.inject.Inject
import com.example.tradeapp.damin.util.Result

class SignInUseCase @Inject constructor(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Boolean> {
        return try {
            val success = repository.signIn(email, password)
            Result.Success(success)
        } catch (e: Exception) {
            Result.Error(Exception("Sign in failed: ${e.message}", e))
        }
    }
}