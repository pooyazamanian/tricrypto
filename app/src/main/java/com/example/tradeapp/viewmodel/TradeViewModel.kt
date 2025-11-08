package com.example.tradeapp.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.tradeapp.damin.model.Asset
import com.example.tradeapp.damin.model.Order
import com.example.tradeapp.damin.model.Trade
import com.example.tradeapp.damin.repository.*
import com.example.tradeapp.damin.repository.TradeRepository
import com.example.tradeapp.viewmodel.intent.TradeEffect
import com.example.tradeapp.viewmodel.intent.TradeIntent
import com.example.tradeapp.viewmodel.intent.TradeState
import com.example.tradeapp.viewmodel.intent.TradeType
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
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
    private val repository: TradeRepository,
    private val supabase: SupabaseClient,
    private val chartRepository: ChartRepository
) : ViewModel()
{

    private val _state = MutableStateFlow(TradeState())
    val state: StateFlow<TradeState> = _state.asStateFlow()

    private val _effect = Channel<TradeEffect>()
    val effect = _effect.receiveAsFlow()

    val userId: UUID? by lazy {
        supabase.auth.currentSessionOrNull()?.user?.id?.let { UUID.fromString(it) }
    }

    init {
        handleIntent(TradeIntent.LoadAssets)
        handleIntent(TradeIntent.LoadUserAssets)
        handleIntent(TradeIntent.LoadProfile)
    }

    fun handleIntent(intent: TradeIntent) {
        when (intent) {
            is TradeIntent.LoadAssets -> loadAssets()
            is TradeIntent.LoadUserAssets -> loadUserAssets()
            is TradeIntent.LoadOrders -> loadOrders()
            is TradeIntent.LoadProfile -> loadProfile()//remove
            is TradeIntent.LoadTradesForAsset -> loadTradesForAsset(intent.assetId)
            is TradeIntent.CreateOrder -> createOrder(intent.order)
            is TradeIntent.UpdateOrderStatus -> updateOrderStatus(intent.orderId, intent.status)
            is TradeIntent.DeleteOrder -> deleteOrder(intent.orderId)
            is TradeIntent.SelectAsset -> selectAsset(intent.asset)
            is TradeIntent.SetTradeType -> setTradeType(intent.type)
            is TradeIntent.CreateTrade -> createTrade(intent.trade)
        }
    }

    private fun loadAssets() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = repository.getAssets()) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            assets = result.data,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message
                        )
                    }
                    _effect.send(TradeEffect.ShowError(result.exception.message ?: "خطا در بارگذاری دارایی‌ها"))
                }
            }
        }
    }

    private fun loadUserAssets() {
        viewModelScope.launch {
            userId?.let { id ->
                _state.update { it.copy(isLoading = true) }
                when (val result = repository.getUserAssets(id)) {
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                userAssets = result.data,
                                error = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.exception.message
                            )
                        }
                        _effect.send(TradeEffect.ShowError(result.exception.message ?: "خطا در بارگذاری دارایی‌های کاربر"))
                    }
                }
            }
        }
    }

    private fun loadOrders() {
        viewModelScope.launch {
            userId?.let { id ->
                _state.update { it.copy(isLoading = true) }
                when (val result = repository.getOrdersForUser(id)) {
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                orders = result.data,
                                error = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.exception.message
                            )
                        }
                        _effect.send(TradeEffect.ShowError(result.exception.message ?: "خطا در بارگذاری سفارشات"))
                    }
                }
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            userId?.let { id ->
                when (val result = repository.getProfile(id)) {
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                profile = result.data,
                                error = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _state.update {
                            it.copy(error = result.exception.message)
                        }
                    }
                }
            }
        }
    }

    private fun loadTradesForAsset(assetId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = repository.getTradesForAsset(UUID.fromString(assetId))) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            trades = result.data,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message
                        )
                    }
                    _effect.send(TradeEffect.ShowError(result.exception.message ?: "خطا در بارگذاری معاملات"))
                }
            }
        }
    }

    private fun createOrder(order: Order) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = repository.createOrder(order)) {
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.send(TradeEffect.ShowSuccess("سفارش با موفقیت ثبت شد"))
                    loadOrders() // بارگذاری مجدد سفارشات
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message
                        )
                    }
                    _effect.send(TradeEffect.ShowError(result.exception.message ?: "خطا در ثبت سفارش"))
                }
            }
        }
    }

    private fun createTrade(trade: Trade) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = repository.createTrade(trade)) {
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.send(TradeEffect.ShowSuccess("معامله با موفقیت انجام شد"))
                    loadUserAssets() // بارگذاری مجدد دارایی‌های کاربر
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message
                        )
                    }
                    _effect.send(TradeEffect.ShowError(result.exception.message ?: "خطا در انجام معامله"))
                }
            }
        }
    }

    private fun updateOrderStatus(orderId: String, status: String) {
        viewModelScope.launch {
            when (val result = repository.updateOrderStatus(UUID.fromString(orderId), status)) {
                is Result.Success -> {
                    _effect.send(TradeEffect.ShowSuccess("وضعیت سفارش به‌روزرسانی شد"))
                    loadOrders()
                }
                is Result.Error -> {
                    _effect.send(TradeEffect.ShowError(result.exception.message ?: "خطا در به‌روزرسانی"))
                }
            }
        }
    }

    private fun deleteOrder(orderId: String) {
        viewModelScope.launch {
            when (val result = repository.deleteOrder(UUID.fromString(orderId))) {
                is Result.Success -> {
                    _effect.send(TradeEffect.ShowSuccess("سفارش حذف شد"))
                    loadOrders()
                }
                is Result.Error -> {
                    _effect.send(TradeEffect.ShowError(result.exception.message ?: "خطا در حذف سفارش"))
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
}