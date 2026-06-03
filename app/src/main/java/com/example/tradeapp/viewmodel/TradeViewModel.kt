package com.example.tradeapp.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.dto.AssetDto
import com.example.tradeapp.dto.TradeDto
import com.example.tradeapp.usecase.CreateTradeUseCase
import com.example.tradeapp.usecase.GetTradesUseCase
import com.example.tradeapp.usecase.ObserveTradesForAssetUseCase
import com.example.tradeapp.viewmodel.effect.TradeEffect
import com.example.tradeapp.viewmodel.intent.TradeIntent
import com.example.tradeapp.viewmodel.state.TradeState
import com.example.tradeapp.viewmodel.state.TradeType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TradeViewModel @Inject constructor(
    private val observeTradesForAssetUseCase: ObserveTradesForAssetUseCase,
    private val getTradesUseCase: GetTradesUseCase,

    private val createTradeUseCase: CreateTradeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TradeState())
    val state: StateFlow<TradeState> = _state.asStateFlow()

    init {
        handleIntent(TradeIntent.LoadTrades)
    }
    private val _effect = Channel<TradeEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleIntent(intent: TradeIntent) {
        when (intent) {
            is TradeIntent.SelectAsset -> selectAsset(intent.asset)
            is TradeIntent.SetTradeType -> setTradeType(intent.type)
//            is TradeIntent.ObserveTradesForAsset -> observeTradesForAsset(intent.assetId)
            is TradeIntent.CreateTrade -> createTrade(intent.trade)
            is TradeIntent.LoadTrades -> observeTrades()
            TradeIntent.ClearTrades -> {
                _state.update {
                    it.copy(
                        trades = emptyList()
                    )
                }
            }
        }
    }

    private fun selectAsset(asset: AssetDto) {
        _state.update { it.copy(selectedAsset = asset) }
    }

    private fun setTradeType(type: TradeType) {
        _state.update { it.copy(tradeType = type) }
    }

    private var tradesJob: Job? = null

//    private fun observeTradesForAsset(assetId: String) {
//
//        handleIntent(TradeIntent.ClearTrades)
//
//        tradesJob?.cancel()
//
//        tradesJob = viewModelScope.launch {
//
//            _state.update { it.copy(isLoading = true) }
//
//            observeTradesForAssetUseCase(UUID.fromString(assetId))
//                .collect { trade ->
//
//                    _state.update { state ->
//
//                        val updatedList = (state.trades + trade)
//
//                        state.copy(
//                            isLoading = false,
//                            trades = updatedList,
//                            error = null
//                        )
//                    }
//                }
//        }
//    }

    //    private fun observeTrades() {
//        handleIntent(TradeIntent.ClearTrades)
//        viewModelScope.launch {
//            _state.update { it.copy(isLoading = true) }
//            when (val result = getTradesUseCase()) {
//                is Result.Success -> {
//                    _state.update {
//                        it.copy(isLoading = false, trades = result.data, error = null)
//                    }
//                }
//
//                is Result.Error -> {
//                    _state.update {
//                        it.copy(isLoading = false, error = result.exception.message)
//                    }
//                    _effect.send(
//                        TradeEffect.ShowError(
//                            result.exception.message ?: "خطا در بارگذاری معاملات"
//                        )
//                    )
//                }
//            }
//        }
//    }
    private fun observeTrades() {

        Log.e("TradeVM", "observeTrades called")

        handleIntent(TradeIntent.ClearTrades)

        tradesJob?.cancel()

        tradesJob = viewModelScope.launch {

            Log.e("TradeVM", "Starting collect")

            _state.update {
                it.copy(isLoading = true)
            }

            observeTradesForAssetUseCase()
                .onStart {
                    Log.e("TradeVM", "Flow started")
                }
                .catch { e ->
                    Log.e(
                        "TradeVM",
                        "Flow error",
                        e
                    )
                }
                .collect { trade ->

                    Log.e(
                        "TradeVM",
                        "Trade received = $trade"
                    )

                    _state.update { state ->

                        val updatedList = state.trades + trade

                        Log.e(
                            "TradeVM",
                            "Trades count before = ${state.trades.size}"
                        )

                        Log.e(
                            "TradeVM",
                            "Trades count after = ${updatedList.size}"
                        )

                        state.copy(
                            isLoading = false,
                            trades = updatedList,
                            error = null
                    )
                }
            }
        }
    }

    private fun createTrade(trade: TradeDto) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = createTradeUseCase(trade)) {
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.send(TradeEffect.ShowSuccess("معامله با موفقیت انجام شد"))
                    // می‌تونی UserAssetViewModel رو از بیرون refresh کنی
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(isLoading = false, error = result.exception.message)
                    }
                    _effect.send(
                        TradeEffect.ShowError(
                            result.exception.message ?: "خطا در انجام معامله"
                        )
                    )
                }
            }
        }
    }
//    private fun selectAsset(asset: Asset) {
//        _state.update { it.copy(selectedAsset = asset) }
//    }
//
//    private fun setTradeType(type: TradeType) {
//        _state.update { it.copy(tradeType = type) }
//    }
}

