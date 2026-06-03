package com.example.tradeapp.usecase
import com.example.tradeapp.dto.TradeDto
import com.example.tradeapp.damin.repository.TradeRepository
import com.example.tradeapp.damin.util.Result
import javax.inject.Inject

class CreateTradeUseCase @Inject constructor(
    private val repository: TradeRepository
) {
    suspend operator fun invoke(trade: TradeDto): Result<List<TradeDto>> {
        return try {
            repository.createTrade(trade)
        } catch (e: Exception) {
            Result.Error(Exception("Failed to create trade: ${e.message}", e))
        }
    }
}