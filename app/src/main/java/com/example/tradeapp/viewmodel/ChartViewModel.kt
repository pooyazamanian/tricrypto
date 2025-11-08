package com.example.tradeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeapp.damin.model.HistoryData
import com.example.tradeapp.damin.repository.ChartRepository
import com.example.tradeapp.damin.repository.TradeRepository
import com.example.tradeapp.viewmodel.intent.ChartIntent
import com.example.tradeapp.viewmodel.intent.TradeType
import com.example.tradeapp.viewmodel.state.ChartState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.tradeapp.damin.repository.Result
import com.example.tradeapp.viewmodel.state.KeyStats
import com.example.tradeapp.viewmodel.state.PriceInfo


sealed class ChartEffect {
    data class ShowError(val message: String) : ChartEffect()
    data class ShowSuccess(val message: String) : ChartEffect()
    data class NavigateToTrade(val assetId: String, val tradeType: TradeType) : ChartEffect()
}
// TimeRange.kt
enum class TimeRange(val hours: Long, val label: String, val resolution: String) {
    ONE_DAY_BEFORE(24, "۱ روز قبل", "5"),
    ONE_DAY(24, "۱ روز", "5"),
    FIVE_DAYS(5 * 24, "۵ روز", "15"),
    ONE_MONTH(30 * 24, "۱ ماه", "60"),
    FIVE_MONTHS(5 * 30 * 24, "۵ ماه", "D"),
    ONE_YEAR(365 * 24, "۱ سال", "D"),
    ALL(Long.MAX_VALUE, "همه", "W")
}
@HiltViewModel
class ChartViewModel @Inject constructor(
    private val chartRepository: ChartRepository,
    private val tradeRepository: TradeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ChartState())
    val state: StateFlow<ChartState> = _state.asStateFlow()

    private val _effect = Channel<ChartEffect>()
    val effect = _effect.receiveAsFlow()

    private var autoRefreshJob: Job? = null

    companion object {
        private const val AUTO_REFRESH_INTERVAL = 60000L
        private const val DEFAULT_SYMBOL = "crypto-bitcoin"
    }

    fun handleIntent(intent: ChartIntent) {
        when (intent) {
            is ChartIntent.LoadChartData -> loadChartData(intent.symbol, intent.timeRange)
            is ChartIntent.LoadAssetInfo -> loadAssetInfo(intent.assetId)
            is ChartIntent.SelectTimeRange -> selectTimeRange(intent.timeRange)
            is ChartIntent.RefreshData -> refreshData()
        }
    }

    private fun loadAssetInfo(assetId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                when (val result = tradeRepository.getAssets()) {
                    is Result.Success -> {
                        // پیدا کردن Asset مورد نظر با ID
                        val selectedAsset = result.data.find { it.id == assetId }

                        if (selectedAsset != null) {
                            _state.update { it.copy(asset = selectedAsset) }

                            // بارگذاری داده‌های نمودار با symbol این asset
                            val symbol = selectedAsset.assetLink ?: DEFAULT_SYMBOL
                            loadChartData(symbol, _state.value.selectedTimeRange)
                        } else {
                            handleError("دارایی پیدا نشد")
                        }
                    }
                    is Result.Error -> {
                        handleError(result.exception.message)
                    }
                }
            } catch (e: Exception) {
                handleError(e.message)
            }
        }
    }

    private fun loadChartData(symbol: String, timeRange: TimeRange) {
        viewModelScope.launch {
            if (_state.value.asset == null) {
                _state.update { it.copy(isLoading = true) }
            } else {
                _state.update { it.copy(isRefreshing = true) }
            }

            val currentTime = System.currentTimeMillis() / 1000
            val fromTime = calculateFromTime(timeRange, currentTime)

            try {
                when (val result = chartRepository.fetchHistory(
                    symbol = "crypto-bitcoin",
                    resolution = "5",
                    from = 1762269322,
                    to = 1762355722
                )) {
                    is Result.Success -> {
                        val data = result.data
                        if (data.s == "ok" && data.t.isNotEmpty()) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    isRefreshing = false,
                                    historyData = data,
                                    priceInfo = data.toPriceInfo(),
                                    keyStats = data.toKeyStats(),
                                    error = null
                                )
                            }
                        } else {
                            handleError("داده‌های نامعتبر از سرور")
                        }
                    }
                    is Result.Error -> {
                        handleError(result.exception.message)
                    }
                }
            } catch (e: Exception) {
                handleError(e.message)
            }
        }
    }

    private fun selectTimeRange(timeRange: TimeRange) {
        _state.update { it.copy(selectedTimeRange = timeRange) }
        val symbol = _state.value.asset?.assetLink ?: DEFAULT_SYMBOL
        loadChartData(symbol, timeRange)
    }

    private fun refreshData() {
        val currentState = _state.value
        val symbol = currentState.asset?.assetLink ?: DEFAULT_SYMBOL
        loadChartData(symbol, currentState.selectedTimeRange)
    }

    private fun calculateFromTime(timeRange: TimeRange, currentTime: Long): Long {
        return when (timeRange) {
            TimeRange.ONE_DAY_BEFORE -> {
                // برای "روز قبل"، از 48 ساعت قبل تا 24 ساعت قبل
                currentTime - (2 * 24 * 60 * 60)
            }
            TimeRange.ALL -> 1750701628
            else -> currentTime - (timeRange.hours * 60 * 60)
        }
    }

    fun startAutoRefresh(intervalMillis: Long = AUTO_REFRESH_INTERVAL) {
        stopAutoRefresh()
        autoRefreshJob = viewModelScope.launch {
            while (isActive) {
                delay(intervalMillis)
                refreshData()
            }
        }
    }

    fun stopAutoRefresh() {
        autoRefreshJob?.cancel()
        autoRefreshJob = null
    }

    private suspend fun handleError(message: String?) {
        _state.update {
            it.copy(
                isLoading = false,
                isRefreshing = false,
                error = message
            )
        }
        _effect.send(ChartEffect.ShowError(message ?: "خطا در بارگذاری داده‌ها"))
    }

    override fun onCleared() {
        super.onCleared()
        stopAutoRefresh()
    }
}

// Extension Functions برای تبدیل HistoryData
private fun HistoryData.toPriceInfo(): PriceInfo {
    val currentPrice = c.lastOrNull() ?: 0.0
    val previousPrice = c.firstOrNull() ?: currentPrice
    val priceChange = currentPrice - previousPrice
    val priceChangePercent = if (previousPrice != 0.0) {
        (priceChange / previousPrice) * 100
    } else {
        0.0
    }

    return PriceInfo(
        currentPrice = currentPrice,
        priceChange = priceChange,
        priceChangePercent = priceChangePercent
    )
}

private fun HistoryData.toKeyStats(): KeyStats {
    return KeyStats(
        openPrice = o.firstOrNull() ?: 0.0,
        closePrice = c.lastOrNull() ?: 0.0,
        highPrice = h.maxOrNull() ?: 0.0,
        lowPrice = l.minOrNull() ?: 0.0,
        volume = v.sum(),
        totalTrades = t.size,
        avgPrice = if (c.isNotEmpty()) c.average() else 0.0
    )
}