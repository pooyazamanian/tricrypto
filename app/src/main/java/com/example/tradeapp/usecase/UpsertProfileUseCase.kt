package com.example.tradeapp.usecase

import com.example.tradeapp.damin.model.Profile
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.damin.repository.ProfileRepository
import javax.inject.Inject

class UpsertProfileUseCase @Inject constructor(
    val repository: ProfileRepository

) {
    suspend operator fun invoke(profile: Profile): Result<String> {
        val result = repository.upsertProfile(profile)
            ?: return Result.Error(Exception("No response from server"))
        return Result.Success("Success")
    }
}