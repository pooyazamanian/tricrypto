package com.example.tradeapp.repository

import com.example.tradeapp.damin.model.UserAsset
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.damin.api.SupabaseClientWrapper
import com.example.tradeapp.damin.repository.WalletRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalletRepositoryImp @Inject constructor(
    private val supabaseClient: SupabaseClientWrapper
): WalletRepository {
    // ---------- UserAssets ----------
    override suspend fun getUserAssets(userId: UUID): Result<List<UserAsset>> = withContext(Dispatchers.IO) {
        try {
            val res = supabaseClient.get(
                "user_assets",
                filter = mapOf("user_id" to userId)
            )
            Result.Success(res.decodeList<UserAsset>())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun upsertUserAsset(userAsset: UserAsset): Result<List<UserAsset>> =
        withContext(Dispatchers.IO) {
            try {
                val res = supabaseClient.post("user_assets", listOf(userAsset))
                Result.Success(res.decodeList<UserAsset>())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
}