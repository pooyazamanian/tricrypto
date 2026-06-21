package com.example.tradeapp.usecase

import com.example.tradeapp.dto.Profile
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.damin.repository.ProfileRepository
import javax.inject.Inject

class UpsertProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(profile: Profile): Result<Unit> {
        return try {
            repository.upsertProfile(profile)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}