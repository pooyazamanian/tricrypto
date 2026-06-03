package com.example.tradeapp.damin.repository

import com.example.tradeapp.dto.UserAsset
import com.example.tradeapp.damin.util.Result
import java.util.UUID

interface WalletRepository {
    suspend fun upsertUserAsset(userAsset: UserAsset): Result<List<UserAsset>>
    suspend fun getUserAssets(userId: UUID): Result<List<UserAsset>>
}