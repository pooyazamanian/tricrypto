package com.example.tradeapp.repository

import com.example.tradeapp.damin.model.Asset
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.damin.api.SupabaseClientWrapper
import com.example.tradeapp.damin.repository.AssetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AssetRepositoryImp @Inject constructor(
    private val supabaseClient: SupabaseClientWrapper
): AssetRepository {
    // ---------- Assets ----------
    override suspend fun getAssets(): Result<List<Asset>> = withContext(Dispatchers.IO) {
        try {
            val res = supabaseClient.get("assets")
            Result.Success(res.decodeList<Asset>())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

//    suspend fun createAsset(asset: Asset): Result<List<Asset>> = withContext(Dispatchers.IO) {
//        try {
//            val res = supabaseClient.post("assets", listOf(asset))
//            Result.Success(res.decodeList<Asset>())
//        } catch (e: Exception) {
//            Result.Error(e)
//        }
//    }



}