package com.example.tradeapp.viewmodel.state

import com.example.tradeapp.dto.Order

data class OrderState(
    val isLoading: Boolean = false,
    val orders: List<Order> = emptyList(),
    val error: String? = null
)