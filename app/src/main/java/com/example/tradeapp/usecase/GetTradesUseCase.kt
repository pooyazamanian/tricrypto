package com.example.tradeapp.usecase

import android.util.Log
import com.example.tradeapp.damin.model.Order
import com.example.tradeapp.damin.model.Trade
import com.example.tradeapp.damin.repository.TradeRepository
import com.example.tradeapp.damin.util.Result
import javax.inject.Inject

class GetTradesUseCase @Inject constructor(
    private val repository: TradeRepository
) {
    suspend operator fun invoke(): Result<List<Trade>>  {
        return try {
            val res = repository.getTrades()
            Log.e("repository.getAssets()", res.toString())
            res
        } catch (e: Exception) {
            Result.Error(Exception("Failed to fetch user assets: ${e.message}", e))
        }
    }
}