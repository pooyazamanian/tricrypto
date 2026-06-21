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
import com.example.tradeapp.viewmodel.util.UiStateWithCatch
import com.example.tradeapp.viewmodel.util.dataOrCached
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
        Log.d(TAG, "ViewModel created \uD83D\uDE80")
        onIntent(WatchlistIntent.Load)
    }

    fun onIntent(intent: WatchlistIntent) {
        when (intent) {
            WatchlistIntent.Load -> {
                load()
            }
            is WatchlistIntent.RefreshPrices -> {
                observePrices(intent.symbols)
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            fetchWatchlist()
            _state.update { it.copy(isRefreshing = false) }
        }
    }

    private fun load() {
        viewModelScope.launch {
            fetchWatchlist()
        }
    }

    private suspend fun fetchWatchlist() {
        Log.d(TAG, "Loading watchlist API...")

        // گرفتن دیتای فعلی (یا کش شده) تا موقع لودینگ صفحه سفید نشه
        val currentData = _state.value.watchlistData.dataOrCached() ?: emptyList()

        // رفتن به حالت لودینگ همراه با نگه داشتن دیتای کش شده
        _state.update {
            it.copy(watchlistData = UiStateWithCatch.Loading(cachedData = currentData))
        }

        when (val res = getWatchlistUseCase()) {
            is Result.Success -> {
                val data = res.data
                val symbols = data.map { "${it.symbol}-USDT" }

                Log.d(TAG, "Watchlist loaded: ${data.size}")

                // موفقیت! دیتای جدید رو به عنوان Success جایگزین می‌کنیم
                _state.update {
                    it.copy(watchlistData = UiStateWithCatch.Success(data))
                }

                // حالا که واچ‌لیست اومد، استریم قیمت‌ها رو استارت می‌زنیم
                onIntent(WatchlistIntent.RefreshPrices(symbols))
            }

            is Result.Error -> {
                Log.e(TAG, "Watchlist error: ${res.exception.message}")

                // ارور! اما دیتای کش شده رو پاس می‌دیم تا کاربر همچنان لیست رو ببینه
                _state.update {
                    it.copy(
                        watchlistData = UiStateWithCatch.Error(
                            message = res.exception.message,
                            cachedData = currentData
                        )
                    )
                }

                _effect.emit(
                    WatchlistEffect.ShowError(res.exception.message ?: "Unknown error")
                )
            }
            else -> Unit
        }
    }

    private fun observePrices(symbols: List<String>) {
        Log.d(TAG, "Starting price stream...")
        priceJob?.cancel()

        priceJob = observePricesUseCase(symbols)
            .onEach { newPrices ->
                // دیتای فعلی واچ‌لیست رو می‌گیریم
                val currentAssets = _state.value.watchlistData.dataOrCached() ?: return@onEach

                // ✨ حل مشکل آپدیت نشدن قیمت‌ها ✨
                val updatedAssets = currentAssets.map { asset ->
                    // توی لیست قیمت‌های جدید می‌گردیم تا سیمبل مربوط به این ارز رو پیدا کنیم
                    // (فرض بر اینه که مدل newPrices فیلدهای symbol و price رو داره. در صورت نیاز تغییرش بده)
                    val matchedPriceItem = newPrices.find {
                        it.symbol == "${asset.symbol}-USDT" || it.symbol == asset.symbol
                    }

                    if (matchedPriceItem != null) {
                        // اگر قیمت جدیدی براش اومده بود، قیمت قبلی رو آپدیت می‌کنیم
                        asset.copy(price = matchedPriceItem.price)
                    } else {
                        // اگر قیمتی براش نیومده بود، همون قبلی رو نگه می‌داریم
                        asset
                    }
                }

                // لیست آپدیت شده رو دوباره به عنوان Success پوش می‌کنیم به UI
                _state.update {
                    it.copy(watchlistData = UiStateWithCatch.Success(updatedAssets))
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel cleared \uD83E\uDDF9")
        priceJob?.cancel()
    }
}
