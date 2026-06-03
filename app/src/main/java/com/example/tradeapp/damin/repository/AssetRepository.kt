package com.example.tradeapp.damin.repository

import com.example.tradeapp.dto.AssetDto
import com.example.tradeapp.damin.util.Result

interface AssetRepository {
    suspend fun getAssets(): Result<List<AssetDto>>
    fun deleteCatch()
}