package com.example.tradeapp.usecase

import com.example.tradeapp.damin.model.UserAsset
import com.example.tradeapp.damin.repository.WalletRepository
import javax.inject.Inject
import com.example.tradeapp.damin.util.Result
import java.util.UUID

class GetUserAssetsUseCase @Inject constructor(
    private val repository: WalletRepository
) {
    suspend operator fun invoke(userId: UUID): Result<List<UserAsset>> {
        return try {
            repository.getUserAssets(userId)
        } catch (e: Exception) {
            Result.Error(Exception("Failed to fetch user assets: ${e.message}", e))
        }
    }
}