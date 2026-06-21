package com.example.tradeapp.viewmodel.state

import com.example.tradeapp.dto.UserAsset

data class UserAssetState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val userAssets: List<UserAsset> = emptyList(),
    val error: String? = null
)