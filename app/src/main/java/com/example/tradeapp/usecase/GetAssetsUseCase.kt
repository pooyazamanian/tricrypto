package com.example.tradeapp.usecase

import android.util.Log
import com.example.tradeapp.dto.AssetDto
import com.example.tradeapp.damin.repository.AssetRepository
import com.example.tradeapp.damin.util.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAssetsUseCase @Inject constructor(
    private val repository: AssetRepository
) {
    suspend operator fun invoke(): Result<List<AssetDto>> {
        return try {
            val res = repository.getAssets()
            Log.e("getAssets()",res.toString())
            res
        } catch (e: Exception) {
            Result.Error(Exception("Failed to fetch assets: ${e.message}", e))
        }
    }
}