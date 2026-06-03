package com.example.tradeapp.repository

import com.example.tradeapp.damin.api.SupabaseClientWrapper
import com.example.tradeapp.dto.MarketTrendDto
import com.example.tradeapp.damin.repository.MarketTrendsRepository
import com.example.tradeapp.damin.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MarketTrendsRepositoryImp @Inject constructor(
    private val supabaseClient: SupabaseClientWrapper
): MarketTrendsRepository {
    override suspend fun getMarketTrends(): Result<List<MarketTrendDto>> = withContext(Dispatchers.IO) {
        try {
            val res = supabaseClient.get("market_trends")
            Result.Success(res.decodeList<MarketTrendDto>())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}