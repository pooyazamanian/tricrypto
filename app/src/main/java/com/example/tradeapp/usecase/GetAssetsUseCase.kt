package com.example.tradeapp.usecase

import com.example.tradeapp.damin.model.Asset
import com.example.tradeapp.damin.repository.AssetRepository
import com.example.tradeapp.damin.util.Result
import javax.inject.Inject

class GetAssetsUseCase @Inject constructor(
    private val repository: AssetRepository
) {
    suspend operator fun invoke(): Result<List<Asset>> {
        return try {
            repository.getAssets()
        } catch (e: Exception) {
            Result.Error(Exception("Failed to fetch assets: ${e.message}", e))
        }
    }
}