package com.example.tradeapp.models

import com.example.tradeapp.dto.AssetDto
import com.example.tradeapp.dto.CreateOrderDto
import com.example.tradeapp.viewmodel.state.OrderType

data class Order(
    val id: String? = null,
    val asset: AssetDto? = null,
    val orderType: OrderType? = null,
    val price: Double? = null,
    val quantity: Double? = null,
    val remainingQuantity: Double? = null
)
fun Order.toCreateOrderDto(): CreateOrderDto =
    CreateOrderDto(
        assetId = asset?.id.orEmpty(),
        orderType = orderType?.name?.lowercase().orEmpty(),
        price = price ?: 0.0,
        quantity = quantity ?: 0.0
    )

fun List<Order>.toCreateOrderDto(): List<CreateOrderDto> =
    map { it.toCreateOrderDto() }