package com.example.tradeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeapp.damin.repository.MarketRepository
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.models.MarketTrend
import com.example.tradeapp.usecase.GetMarketTrendUseCase
import com.example.tradeapp.usecase.ObserveMarketBySymbolsUseCase
import com.example.tradeapp.viewmodel.state.MarketTrendsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketTrendsViewModel @Inject constructor(
    private val getMarketTrendUseCase: GetMarketTrendUseCase,
    private val observeMarketUseCase: ObserveMarketBySymbolsUseCase,
) : ViewModel() {

    private val _state =
        MutableStateFlow(MarketTrendsState())

    val state = _state.asStateFlow()

    private var pricesJob: Job? = null

    init {
        loadData()
    }

    private fun loadData() {

        viewModelScope.launch {

            _state.update {
                it.copy(isLoading = true)
            }

            when (val result = getMarketTrendUseCase()) {

                is Result.Success -> {

                    val trends = result.data

                    val symbols = trends
                        .mapNotNull { it.assetId }
                        .map { "${it}-USDT" }

                    _state.update {
                        it.copy(
                            isLoading = false,
                            trends = trends
                        )
                    }

                    observePrices(symbols)
                }

                is Result.Error -> {

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message
                        )
                    }
                }

                else -> Unit
            }
        }
    }


    private fun observePrices(symbols: List<String>) {

        pricesJob?.cancel()

        pricesJob =
            observeMarketUseCase(symbols)
                .onEach { prices ->

                    _state.update { state ->

                        val updated = state.trends.map { trend ->

                            trend.copy(
                                price = trend.price
                            )
                        }

                        state.copy(trends = updated)
                    }
                }
                .launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        pricesJob?.cancel()
    }
}

