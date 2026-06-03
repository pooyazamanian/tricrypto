package com.example.tradeapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.usecase.GetUserWatchlistWithAssetsUseCase
import com.example.tradeapp.usecase.ObserveMarketBySymbolsUseCase
import com.example.tradeapp.viewmodel.effect.WatchlistEffect
import com.example.tradeapp.viewmodel.intent.WatchlistIntent
import com.example.tradeapp.viewmodel.state.WatchlistState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val getWatchlistUseCase: GetUserWatchlistWithAssetsUseCase,
    private val observePricesUseCase: ObserveMarketBySymbolsUseCase,
) : ViewModel() {

    private val TAG = "WatchlistVM"

    private val _state = MutableStateFlow(WatchlistState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<WatchlistEffect>()
    val effect = _effect.asSharedFlow()

    private var priceJob: Job? = null

    init {
        Log.d(TAG, "ViewModel created 🚀")
        onIntent(WatchlistIntent.Load)
    }

    fun onIntent(intent: WatchlistIntent) {
        when (intent) {

            WatchlistIntent.Load -> {
                Log.d(TAG, "Intent: Load")
                load()
            }

            is WatchlistIntent.RefreshPrices -> {
                Log.d(TAG, "Intent: RefreshPrices -> ${intent.symbols}")
                observePrices(intent.symbols)
            }
        }
    }

    private fun load() {

        viewModelScope.launch {

            Log.d(TAG, "Loading watchlist API...")

            _state.update { it.copy(isLoading = true) }

            when (val res = getWatchlistUseCase()) {

                is Result.Success -> {

                    val data = res.data

                    val symbols =
                        data.map { "${it.symbol}-USDT" }

                    Log.d(TAG, "Watchlist loaded: ${data.size}")
                    Log.d(TAG, "Watchlist data: ${data.first()}")

                    Log.d(TAG, "Symbols: $symbols")

                    _state.update {
                        it.copy(
                            isLoading = false,
                            assets = data
                        )
                    }

                    onIntent(
                        WatchlistIntent.RefreshPrices(symbols)
                    )
                }

                is Result.Error -> {

                    Log.e(TAG, "Watchlist error: ${res.exception.message}")

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = res.exception.message
                        )
                    }

                    _effect.emit(
                        WatchlistEffect.ShowError(
                            res.exception.message ?: "Unknown error"
                        )
                    )
                }

                else -> Unit
            }
        }
    }

    private fun observePrices(symbols: List<String>) {

        Log.d(TAG, "Starting price stream...")

        priceJob?.cancel()

        priceJob =
            observePricesUseCase(symbols)
                .onEach { prices ->
                    Log.d(TAG, "Watchlist data: ${prices.firstOrNull()}")

                    Log.d(TAG, "Prices update received: ${prices.size}")

                    _state.update { state ->

                        state.copy(
                            assets = state.assets.map { asset ->

                                asset.copy(
                                    price = asset.price
                                )
                            }
                        )
                    }
                }
                .launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()

        Log.d(TAG, "ViewModel cleared 🧹")

        priceJob?.cancel()
    }
}
