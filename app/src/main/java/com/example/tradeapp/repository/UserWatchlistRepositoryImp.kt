package com.example.tradeapp.repository

import android.util.Log
import com.example.tradeapp.damin.api.SupabaseClientWrapper
import com.example.tradeapp.dto.UserWatchlistWithAssetDto
import com.example.tradeapp.damin.repository.UserWatchlistRepository
import com.example.tradeapp.damin.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UserWatchlistRepositoryImp @Inject constructor(
    private val supabaseClient: SupabaseClientWrapper
): UserWatchlistRepository {
    override suspend fun getUserWatchlistWithAssets(): Result<List<UserWatchlistWithAssetDto>> = withContext(Dispatchers.IO) {
        try {
            val res = supabaseClient.get("user_watchlist_with_assets")

            Log.d("UserWatchlistRepository", "UserWatchlistRepositoryImp.getUserWatchlist: ${res.decodeList<UserWatchlistWithAssetDto>()}")
            Result.Success(res.decodeList<UserWatchlistWithAssetDto>())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}