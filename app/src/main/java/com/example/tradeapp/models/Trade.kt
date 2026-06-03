package com.example.tradeapp.models

import com.example.tradeapp.dto.AssetDto

data class Trade(
    val price: Double? = null,
    val quantity: Double? = null,
    val asset: AssetDto? = null,
)