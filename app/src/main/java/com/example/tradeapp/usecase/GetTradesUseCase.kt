package com.example.tradeapp.usecase

import android.util.Log
import com.example.tradeapp.dto.TradeDto
import com.example.tradeapp.damin.repository.TradeRepository
import com.example.tradeapp.damin.util.Result
import javax.inject.Inject

class GetTradesUseCase @Inject constructor(
    private val repository: TradeRepository
) {
    suspend operator fun invoke(): Result<List<TradeDto>>  {
        return try {
            val res = repository.getTrades()
            Log.e("repository.getTrades()", res.toString())
            res
        } catch (e: Exception) {
            Result.Error(Exception("Failed to fetch user assets: ${e.message}", e))
        }
    }
}