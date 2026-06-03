package com.example.tradeapp.damin.repository

import com.example.tradeapp.dto.MarketTrendDto
import com.example.tradeapp.damin.util.Result

interface MarketTrendsRepository {
    suspend fun getMarketTrends(): Result<List<MarketTrendDto>>
}