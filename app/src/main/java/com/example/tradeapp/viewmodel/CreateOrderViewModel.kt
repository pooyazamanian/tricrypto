package com.example.tradeapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.dto.AssetDto
import com.example.tradeapp.models.Order
import com.example.tradeapp.usecase.CreateOrderUseCase
import com.example.tradeapp.viewmodel.effect.CreatingOrderEffect
import com.example.tradeapp.viewmodel.intent.OrderingIntent
import com.example.tradeapp.viewmodel.state.OrderingState
import com.example.tradeapp.viewmodel.state.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateOrderViewModel @Inject constructor(
    private val createOrderUseCase: CreateOrderUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(OrderingState())
    val state: StateFlow<OrderingState> = _state.asStateFlow()


    fun handleIntent(intent: OrderingIntent) {
        when (intent) {
            is OrderingIntent.SelectAsset -> selectAsset(intent.asset)
            is OrderingIntent.SetTradeType -> setOrderType(intent.type)
            is OrderingIntent.CreateOrder -> createOrder(intent.order)

        }
    }

    private val _effect = Channel<CreatingOrderEffect>()
    val effect = _effect.receiveAsFlow()
    private fun selectAsset(asset: AssetDto) {
        _state.update { it.copy(selectedAsset = asset) }
    }

    private fun setOrderType(type: OrderType) {
        _state.update { it.copy(orderType = type) }
    }

    private fun createOrder(order: Order) {

        Log.d(
            "CreateOrderVM",
            "received order = $order"
        )

        viewModelScope.launch {

            _state.update {
                it.copy(isLoading = true)
            }

            val finalOrder =
                order.copy(
                    asset = _state.value.selectedAsset
                )

            Log.d(
                "CreateOrderVM",
                "final order = $finalOrder"
            )

            when (
                val result =
                    createOrderUseCase(finalOrder)
            ) {

                is Result.Success -> {

                    Log.d(
                        "CreateOrderVM",
                        "order created successfully"
                    )

                    _state.update {
                        it.copy(isLoading = false)
                    }

                    _effect.send(
                        CreatingOrderEffect.ShowSuccess(
                            "سفارش با موفقیت ثبت شد"
                        )
                    )
                }

                is Result.Error -> {

                    Log.e(
                        "CreateOrderVM",
                        "create order failed",
                        result.exception
                    )

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message
                        )
                    }

                    _effect.send(
                        CreatingOrderEffect.ShowError(
                            result.exception.message
                                ?: "خطا در ثبت سفارش"
                        )
                    )
                }
            }
        }
    }

}