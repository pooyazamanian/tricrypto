package com.example.tradeapp.models

import com.example.tradeapp.dto.AssetDto
import kotlinx.serialization.SerialName

data class MarketTrend(
    val asset: AssetDto? = null,
    val trendType: String? = null,
    val title: String? = null,
    val rank: Int? = null,
    val isActive: Boolean? = null,
    val price: Double? = null
)