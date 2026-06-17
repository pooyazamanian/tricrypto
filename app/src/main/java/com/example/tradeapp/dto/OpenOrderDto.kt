package com.example.tradeapp.dto

import com.example.tradeapp.models.Order
import com.example.tradeapp.viewmodel.state.OrderType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenOrderDto(

    @SerialName("id")
    val id: String?,

    @SerialName("order_type")
    val orderType: String?,

    @SerialName("price")
    val price: Double?,

    @SerialName("remaining_quantity")
    val quantity: Double?,

    @SerialName("asset")
    val asset: AssetDto?
)

fun OpenOrderDto.toOrder(): Order {

    return Order(
        id = id,
        asset = asset,
        orderType = orderType?.let {
            runCatching {
                OrderType.valueOf(it.uppercase())
            }.getOrNull()
        },
        price = price,
        quantity = quantity,
        remainingQuantity = quantity
    )
}
fun List<OpenOrderDto>.toOrders(): List<Order> =
    map { it.toOrder() }