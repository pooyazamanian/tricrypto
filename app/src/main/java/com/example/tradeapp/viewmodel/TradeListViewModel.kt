package com.example.tradeapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeapp.usecase.ObserveTradesForAssetUseCase
import com.example.tradeapp.viewmodel.intent.TradeListIntent
import com.example.tradeapp.viewmodel.state.TradeListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TradeListViewModel @Inject constructor(
    private val observeTradesForAssetUseCase: ObserveTradesForAssetUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(TradeListState())
    val state: StateFlow<TradeListState> = _state.asStateFlow()

    private var tradesJob: Job? = null

    init {
        handleIntent(TradeListIntent.LoadTrades)
    }

    fun handleIntent(intent: TradeListIntent) {
        when (intent) {
            is TradeListIntent.LoadTrades -> observeTrades()
            TradeListIntent.ClearTrades -> {
                _state.update { it.copy(trades = emptyList()) }
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            observeTrades()
            // Real-time flow, wait a bit for initial sync or simulation
            delay(1000)
            _state.update { it.copy(isRefreshing = false) }
        }
    }

    private fun observeTrades() {
        Log.e("TradeVM", "observeTrades called")
        handleIntent(TradeListIntent.ClearTrades)
        tradesJob?.cancel()

        tradesJob = viewModelScope.launch {
            Log.e("TradeVM", "Starting collect")
            _state.update { it.copy(isLoading = true) }

            observeTradesForAssetUseCase()
                .onStart { Log.e("TradeVM", "Flow started") }
                .catch { e -> Log.e("TradeVM", "Flow error", e) }
                .collect { trade ->
                    Log.e("TradeVM", "Trade received = $trade")
                    _state.update { state ->
                        val updatedList = state.trades + trade
                        state.copy(
                            isLoading = false,
                            trades = updatedList,
                            error = null
                        )
                    }
                }
        }
    }
}
