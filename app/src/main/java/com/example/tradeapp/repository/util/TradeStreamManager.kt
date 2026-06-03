package com.example.tradeapp.repository.util

import android.util.Log
import com.example.tradeapp.damin.api.SupabaseClientWrapper
import com.example.tradeapp.dto.TradeDto
import io.github.jan.supabase.realtime.PostgresAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TradeStreamManager @Inject constructor(
    private val supabaseClient: SupabaseClientWrapper
) {

    private var currentAssetId: UUID? = null
    private var observeJob: Job? = null

    private val _trades = MutableStateFlow<List<TradeDto>>(emptyList())
    val trades: StateFlow<List<TradeDto>> = _trades

    fun connect(
        scope: CoroutineScope,
        assetId: UUID?,
        limit: Int = 50
    ) {

        Log.e("TradeStream", "connect called")
        Log.e("TradeStream", "assetId = $assetId")
        Log.e("TradeStream", "currentAssetId = $currentAssetId")
        Log.e("TradeStream", "cache size = ${_trades.value.size}")

        if (
            currentAssetId == assetId &&
            _trades.value.isNotEmpty()
        ) {
            Log.e("TradeStream", "already connected -> skip")
            return
        }

        disconnect()

        currentAssetId = assetId

        observeJob = scope.launch {

            Log.e("TradeStream", "launch started")

            try {

                loadInitialTrades(
                    assetId = assetId,
                    limit = limit
                )

                Log.e(
                    "TradeStream",
                    "initial loaded size = ${_trades.value.size}"
                )

            } catch (e: Exception) {

                Log.e(
                    "TradeStream",
                    "loadInitialTrades failed",
                    e
                )
            }

            try {

                Log.e(
                    "TradeStream",
                    "starting realtime observe"
                )

                supabaseClient
                    .observeTable(
                        table = "trades",
                        filter = assetId?.let {
                            mapOf("asset_id" to it)
                        } ?: emptyMap()
                    )
                    .collect { action ->

                        Log.e(
                            "TradeStream",
                            "action received = ${action::class.simpleName}"
                        )

                        when (action) {

                            is PostgresAction.Insert -> {

                                Log.e(
                                    "TradeStream",
                                    "insert record = ${action.record}"
                                )

                                try {

                                    val trade =
                                        Json.decodeFromJsonElement<TradeDto>(
                                            action.record
                                        )

                                    Log.e(
                                        "TradeStream",
                                        "decoded trade = $trade"
                                    )

                                    _trades.update { current ->

                                        Log.e(
                                            "TradeStream",
                                            "before update size = ${current.size}"
                                        )

                                        buildList {

                                            add(trade)
                                            addAll(current)

                                        }.take(limit)
                                    }

                                    Log.e(
                                        "TradeStream",
                                        "after update size = ${_trades.value.size}"
                                    )

                                } catch (e: Exception) {

                                    Log.e(
                                        "TradeStream",
                                        "decode failed",
                                        e
                                    )
                                }
                            }

                            else -> {
                                Log.e(
                                    "TradeStream",
                                    "ignored action = ${action::class.simpleName}"
                                )
                            }
                        }
                    }

            } catch (e: Exception) {

                Log.e(
                    "TradeStream",
                    "observeTable failed",
                    e
                )
            }
        }
    }

    fun disconnect() {

        Log.e(
            "TradeStream",
            "disconnect called"
        )

        observeJob?.cancel()
        observeJob = null
    }

    private suspend fun loadInitialTrades(
        assetId: UUID?,
        limit: Int
    ) {

        Log.e(
            "TradeStream",
            "loadInitialTrades started"
        )

        val result = supabaseClient.get(
            table = "trades",
            filter = assetId?.let {
                mapOf("asset_id" to it)
            } ?: emptyMap(),
            limit = limit.toLong(),
            orderActive = true
        )

        val decoded =
            result.decodeList<TradeDto>()

        Log.e(
            "TradeStream",
            "decoded size = ${decoded.size}"
        )

        _trades.value = decoded

        Log.e(
            "TradeStream",
            "stateflow updated"
        )
    }
}