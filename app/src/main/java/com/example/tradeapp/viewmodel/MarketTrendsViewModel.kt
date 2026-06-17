package com.example.tradeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeapp.damin.repository.MarketRepository
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.models.MarketTrend
import com.example.tradeapp.usecase.GetMarketTrendUseCase
import com.example.tradeapp.usecase.ObserveMarketBySymbolsUseCase
import com.example.tradeapp.viewmodel.state.MarketTrendsIntent
import com.example.tradeapp.viewmodel.state.MarketTrendsState
import com.example.tradeapp.viewmodel.util.UiStateWithCatch
import com.example.tradeapp.viewmodel.util.dataOrCached
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

    private val _state = MutableStateFlow(MarketTrendsState())
    val state = _state.asStateFlow()

    private var pricesJob: Job? = null

    init {
        // شروع کار با ارسال Intent
        onIntent(MarketTrendsIntent.Load)
    }

    // هندل کردن Intentها (معماری MVI)
    fun onIntent(intent: MarketTrendsIntent) {
        when (intent) {
            MarketTrendsIntent.Load -> loadData()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            // گرفتن دیتای قبلی برای نمایش در حین لودینگ
            val currentData = _state.value.trendsData.dataOrCached() ?: emptyList()

            _state.update {
                it.copy(trendsData = UiStateWithCatch.Loading(cachedData = currentData))
            }

            when (val result = getMarketTrendUseCase()) {
                is Result.Success -> {
                    val trends = result.data
                    val symbols = trends
                        .mapNotNull { it.asset?.symbol }
                        .map { "${it}-USDT" } // تبدیل به فرمت سیمبل‌های صرافی

                    // دیتای جدید با موفقیت دریافت شد
                    _state.update {
                        it.copy(trendsData = UiStateWithCatch.Success(trends))
                    }

                    // استریم قیمت‌ها رو برای این ارزها استارت می‌زنیم
                    if (symbols.isNotEmpty()) {
                        observePrices(symbols)
                    }
                }

                is Result.Error -> {
                    // در صورت ارور، دیتای کش شده رو نگه می‌داریم و ارور رو ست می‌کنیم
                    _state.update {
                        it.copy(
                            trendsData = UiStateWithCatch.Error(
                                message = result.exception.message,
                                cachedData = currentData
                            )
                        )
                    }
                }

                else -> Unit
            }
        }
    }

    private fun observePrices(symbols: List<String>) {
        pricesJob?.cancel()

        pricesJob = observeMarketUseCase(symbols)
            .onEach { newPrices ->

                // لیست فعلی ترندها رو می‌گیریم
                val currentTrends = _state.value.trendsData.dataOrCached() ?: return@onEach

                // ✨ حل باگ قیمت‌ها ✨
                val updatedTrends = currentTrends.map { trend ->
                    // تطبیق دادن ارز ترند با لیستی که از وب‌سوکت/استریم اومده
                    val matchedPrice = newPrices.find {
                        it.symbol == "${trend.asset?.symbol}-USDT" || it.symbol == trend.asset?.symbol
                    }

                    if (matchedPrice != null) {
                        trend.copy(price = matchedPrice.price) // جایگذاری قیمت واقعی
                    } else {
                        trend
                    }
                }

                _state.update { state ->
                    state.copy(trendsData = UiStateWithCatch.Success(updatedTrends))
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        pricesJob?.cancel()
    }
}

