package com.example.tradeapp.viewmodel.state

import com.example.tradeapp.damin.model.Asset

data class AssetState(
    val isLoading: Boolean = false,
    val assets: List<Asset> = emptyList(),
    val error: String? = null
)