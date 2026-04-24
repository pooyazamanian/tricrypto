package com.example.tradeapp.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.tradeapp.damin.model.Asset
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.damin.model.Trade
import com.example.tradeapp.usecase.CreateTradeUseCase
import com.example.tradeapp.usecase.GetTradesForAssetUseCase
import com.example.tradeapp.usecase.GetTradesUseCase
import com.example.tradeapp.viewmodel.effect.TradeEffect
import com.example.tradeapp.viewmodel.intent.TradeIntent
import com.example.tradeapp.viewmodel.state.TradeState
import com.example.tradeapp.viewmodel.state.TradeType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class TradeViewModel @Inject constructor(
    private val getTradesUseForAssetCase: GetTradesForAssetUseCase,
    private val getTradesUseCase: GetTradesUseCase,

    private val createTradeUseCase: CreateTradeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TradeState())
    val state: StateFlow<TradeState> = _state.asStateFlow()

    private val _effect = Channel<TradeEffect>()
    val effect = _effect.receiveAsFlow()

    fun handleIntent(intent: TradeIntent) {
        when (intent) {
            is TradeIntent.SelectAsset -> selectAsset(intent.asset)
            is TradeIntent.SetTradeType -> setTradeType(intent.type)
            is TradeIntent.LoadTradesForAsset -> loadTradesForAsset(intent.assetId)
            is TradeIntent.CreateTrade -> createTrade(intent.trade)
            is TradeIntent.LoadTrades -> loadTrades()
            TradeIntent.ClearTrades -> {
                _state.update {
                    it.copy(
                        trades = emptyList()
                    )
                }
            }
        }
    }

    private fun selectAsset(asset: Asset) {
        _state.update { it.copy(selectedAsset = asset) }
    }

    private fun setTradeType(type: TradeType) {
        _state.update { it.copy(tradeType = type) }
    }

    private fun loadTradesForAsset(assetId: String) {
        handleIntent(TradeIntent.ClearTrades)
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getTradesUseForAssetCase(UUID.fromString(assetId))) {
                is Result.Success -> {
                    _state.update {
                        it.copy(isLoading = false, trades = result.data, error = null)
                    }
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(isLoading = false, error = result.exception.message)
                    }
                    _effect.send(
                        TradeEffect.ShowError(
                            result.exception.message ?: "خطا در بارگذاری معاملات"
                        )
                    )
                }
            }
        }
    }

    private fun loadTrades() {
        handleIntent(TradeIntent.ClearTrades)
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getTradesUseCase()) {
                is Result.Success -> {
                    _state.update {
                        it.copy(isLoading = false, trades = result.data, error = null)
                    }
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(isLoading = false, error = result.exception.message)
                    }
                    _effect.send(
                        TradeEffect.ShowError(
                            result.exception.message ?: "خطا در بارگذاری معاملات"
                        )
                    )
                }
            }
        }
    }

    private fun createTrade(trade: Trade) {
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

