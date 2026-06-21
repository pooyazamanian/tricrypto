package com.example.tradeapp.usecase
import com.example.tradeapp.damin.repository.AuthenticationRepository
import javax.inject.Inject
import com.example.tradeapp.damin.util.Result
class SignUpUseCase @Inject constructor(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return try {
            val success = repository.signUp(email, password)
            success
        } catch (e: Exception) {
            Result.Error(Exception("Sign up failed: ${e.message}", e))
        }
    }
}