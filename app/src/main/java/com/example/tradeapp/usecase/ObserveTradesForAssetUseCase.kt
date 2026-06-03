package com.example.tradeapp.usecase

import android.util.Log
import com.example.tradeapp.damin.repository.AssetRepository
import com.example.tradeapp.damin.repository.TradeRepository
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.dto.AssetDto
import com.example.tradeapp.models.Trade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class ObserveTradesForAssetUseCase @Inject constructor(
    private val repository: TradeRepository,
    private val assetRepository: AssetRepository
) {
    private val assets = mutableMapOf<String, AssetDto>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            when (val result = assetRepository.getAssets()) {
                is Result.Error -> {
                    Log.e("TradeUseCase", "error ${result.exception}")
                }
                is Result.Success -> {
                    Log.e("TradeUseCase", "success ${result.data}")
                    putAll(result.data)
                }
            }
        }
    }

    operator fun invoke(
        assetId: UUID? = null
    ): Flow<List<Trade>> {

        return repository
            .observeTradesForAsset(assetId)
            .map { trades ->

                Log.e("TradeUseCase", "Trades received = ${trades.size}")
                trades.mapNotNull { trade ->
                    Log.e("TradeUseCase", "Trades received = ${trade}")
                    while (assets.isEmpty()) {
                        delay(5000)
                        Log.e("TradeUseCase", "delay")
                    }
                    val asset = get(trade.assetId)
                    Log.e("TradeUseCase", "asset got $asset")

                    asset?.let {
                        Trade(
                            price = trade.price,
                            quantity = trade.quantity,
                            asset = it
                        )
                    }
                }
            }
    }

    fun putAll(list: List<AssetDto>) {
        list.forEach {
            it.id?.let { id ->
                assets[id] = it
            }
        }
    }

    fun get(id: String?): AssetDto? {
        return id?.let { assets[it] }
    }
}