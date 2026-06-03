package com.example.tradeapp.usecase

import android.util.Log
import com.example.tradeapp.dto.MarketTrendDto
import com.example.tradeapp.damin.repository.MarketTrendsRepository
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.dto.toDomain
import com.example.tradeapp.models.MarketTrend
import javax.inject.Inject

class GetMarketTrendUseCase @Inject constructor(
    private val repository: MarketTrendsRepository
) {

    suspend operator fun invoke(): Result<List<MarketTrend>> {

        return when (val result = repository.getMarketTrends()) {
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