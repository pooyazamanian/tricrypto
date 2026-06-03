package com.example.tradeapp.damin.repository

import com.example.tradeapp.dto.UserWatchlistWithAssetDto
import com.example.tradeapp.damin.util.Result

interface UserWatchlistRepository {

    suspend fun getUserWatchlistWithAssets(): Result<List<UserWatchlistWithAssetDto>>
}