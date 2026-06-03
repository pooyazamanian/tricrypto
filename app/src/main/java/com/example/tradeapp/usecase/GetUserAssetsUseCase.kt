package com.example.tradeapp.usecase

import android.util.Log
import com.example.tradeapp.dto.UserAsset
import com.example.tradeapp.damin.repository.WalletRepository
import javax.inject.Inject
import com.example.tradeapp.damin.util.Result
import java.util.UUID

class GetUserAssetsUseCase @Inject constructor(
    private val repository: WalletRepository
) {
    suspend operator fun invoke(userId: UUID): Result<List<UserAsset>> {
        return try {
            val res = repository.getUserAssets(userId)
            Log.e("getUserAssets()",res.toString())
            res

        } catch (e: Exception) {
            Result.Error(Exception("Failed to fetch user assets: ${e.message}", e))
        }
    }
}