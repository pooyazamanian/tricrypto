package com.example.tradeapp.usecase
import com.example.tradeapp.damin.model.Trade
import com.example.tradeapp.damin.repository.TradeRepository
import com.example.tradeapp.damin.util.Result
import java.util.UUID
import javax.inject.Inject

class GetTradesForAssetUseCase @Inject constructor(
    private val repository: TradeRepository
) {
    suspend operator fun invoke(assetId: UUID, limit: Int = 50): Result<List<Trade>> {
        return try {
            repository.getTradesForAsset(assetId, limit)
        } catch (e: Exception) {
            Result.Error(Exception("Failed to fetch trades: ${e.message}", e))
        }
    }
}