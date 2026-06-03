package com.example.tradeapp.usecase

import com.example.tradeapp.damin.repository.UserWatchlistRepository
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.dto.toDomain
import com.example.tradeapp.models.UserWatchlist
import javax.inject.Inject

class GetUserWatchlistWithAssetsUseCase @Inject constructor(
    private val repository: UserWatchlistRepository
) {
    suspend operator fun invoke(): Result<List<UserWatchlist>> {
        return when (val result = repository.getUserWatchlistWithAssets()) {
            is Result.Success -> {
                Result.Success(
                    result.data.toDomain()
                )
            }
            is Result.Error -> {
                result
            }
        }
    }
}