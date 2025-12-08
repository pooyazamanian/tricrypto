package com.example.tradeapp.usecase
import com.example.tradeapp.damin.repository.AuthenticationRepository
import com.example.tradeapp.damin.util.Result
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Boolean> {
        return try {
            val success = repository.signUp(email, password)
            Result.Success(success)
        } catch (e: Exception) {
            Result.Error(Exception("Sign up failed: ${e.message}", e))
        }
    }
}