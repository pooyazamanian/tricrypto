package com.example.tradeapp.usecase

import com.example.tradeapp.damin.model.Profile
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.damin.repository.ProfileRepository
import java.util.UUID
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: UUID): Result<Profile?> {
        val result = repository.getProfile(userId)
            ?: return Result.Error(Exception("No response from server"))
        val profile = try {
            result.decodeSingleOrNull<Profile>()
        } catch (e: Exception) {
            null
        }

        return Result.Success(profile)
    }
}