package com.example.tradeapp.damin.repository

import com.example.tradeapp.damin.model.Asset
import com.example.tradeapp.damin.util.Result

interface AssetRepository {
    suspend fun getAssets(): Result<List<Asset>>
}