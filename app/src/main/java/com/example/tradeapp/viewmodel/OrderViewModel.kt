package com.example.tradeapp.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeapp.damin.model.Order
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.usecase.CreateOrderUseCase
import com.example.tradeapp.usecase.GetOrdersForUserUseCase
import com.example.tradeapp.usecase.UpdateOrderStatusUseCase
import com.example.tradeapp.viewmodel.effect.OrderEffect
import com.example.tradeapp.viewmodel.intent.OrderIntent
import com.example.tradeapp.viewmodel.state.OrderState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersForUserUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase,
    private val supabase: SupabaseClient
) : ViewModel() {

    private val _state = MutableStateFlow(OrderState())
    val state: StateFlow<OrderState> = _state.asStateFlow()

    private val _effect = Channel<OrderEffect>()
    val effect = _effect.receiveAsFlow()

    private val userId: UUID? by lazy {
        supabase.auth.currentSessionOrNull()?.user?.id?.let { UUID.fromString(it) }
    }

    init {
        handleIntent(OrderIntent.LoadOrders)
    }

    fun handleIntent(intent: OrderIntent) {
        when (intent) {
            OrderIntent.LoadOrders -> loadOrders()
            is OrderIntent.CreateOrder -> createOrder(intent.order)
            is OrderIntent.UpdateOrderStatus -> updateOrderStatus(intent.orderId, intent.status)
        }
    }

    private fun loadOrders() {
        viewModelScope.launch {
            userId?.let { id ->
                _state.update { it.copy(isLoading = true) }
                when (val result = getOrdersUseCase(id)) {
                    is Result.Success -> {
                        _state.update {
                            it.copy(isLoading = false, orders = result.data, error = null)
                        }
                    }
                    is Result.Error -> {
                        _state.update {
                            it.copy(isLoading = false, error = result.exception.message)
                        }
                        _effect.send(OrderEffect.ShowError(result.exception.message ?: "خطا در بارگذاری سفارشات"))
                    }
                }
            }
        }
    }

    private fun createOrder(order: Order) {
        viewModelScope.launch {
            userId?.let { id ->
                _state.update { it.copy(isLoading = true) }
                when (val result = createOrderUseCase(order.copy(userId = id.toString() ))) {
                    is Result.Success -> {
                        _state.update { it.copy(isLoading = false) }
                        _effect.send(OrderEffect.ShowSuccess("سفارش با موفقیت ثبت شد"))
                        loadOrders()
                    }
                    is Result.Error -> {
                        _state.update {
                            it.copy(isLoading = false, error = result.exception.message)
                        }
                        _effect.send(OrderEffect.ShowError(result.exception.message ?: "خطا در ثبت سفارش"))
                    }
                }
            }
        }
    }

    private fun updateOrderStatus(orderId: String, status: String) {
        viewModelScope.launch {
            when (val result = updateOrderStatusUseCase(UUID.fromString(orderId), status)) {
                is Result.Success -> {
                    _effect.send(OrderEffect.ShowSuccess("وضعیت سفارش به‌روزرسانی شد"))
                    loadOrders()
                }
                is Result.Error -> {
                    _effect.send(OrderEffect.ShowError(result.exception.message ?: "خطا در به‌روزرسانی"))
                }
            }
        }
    }

//    private fun deleteOrder(orderId: String) {
//        viewModelScope.launch {
//            when (val result = deleteOrderUseCase(UUID.fromString(orderId))) {
//                is Result.Success -> {
//                    _effect.send(OrderEffect.ShowSuccess("سفارش حذف شد"))
//                    loadOrders()
//                }
//                is Result.Error -> {
//                    _effect.send(OrderEffect.ShowError(result.exception.message ?: "خطا در حذف سفارش"))
//                }
//            }
//        }
//    }
}