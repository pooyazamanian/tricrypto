package com.example.tradeapp.dto

import com.example.tradeapp.models.Order
import com.example.tradeapp.viewmodel.state.OrderType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderDto(
    @SerialName("asset_id")
    val assetId: String,

    @SerialName("order_type")
    val orderType: String,

    @SerialName("price")
    val price: Double,

    @SerialName("quantity")
    val quantity: Double
)
fun CreateOrderDto.toOrder(
    asset: AssetDto? = null
): Order {

    return Order(
        asset = asset,
        orderType = runCatching {
            OrderType.valueOf(
                orderType.uppercase()
            )
        }.getOrNull(),
        price = price,
        quantity = quantity,
        remainingQuantity = quantity
    )
}