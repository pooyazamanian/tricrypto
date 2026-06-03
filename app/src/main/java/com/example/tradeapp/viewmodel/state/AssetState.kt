package com.example.tradeapp.viewmodel.state

import com.example.tradeapp.dto.AssetDto

data class AssetState(
    val isLoading: Boolean = false,
    val assets: List<AssetDto> = emptyList(),
    val error: String? = null
)