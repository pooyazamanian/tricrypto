package com.example.tradeapp.repository

import com.example.tradeapp.dto.AssetDto
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.damin.api.SupabaseClientWrapper
import com.example.tradeapp.damin.repository.AssetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AssetRepositoryImp @Inject constructor(
    private val supabaseClient: SupabaseClientWrapper
): AssetRepository {
    var catch:Result<List<AssetDto>>? = null
    // ---------- Assets ----------
    override suspend fun getAssets(): Result<List<AssetDto>> = withContext(Dispatchers.IO) {
        try {
            if(catch == null){
                val res = supabaseClient.get("assets")
                catch = Result.Success(res.decodeList<AssetDto>())
                catch!!
            }else{
                catch!!
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun deleteCatch(){
        catch = null
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