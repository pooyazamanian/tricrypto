package com.example.tradeapp.usecase
import android.util.Log
import com.example.tradeapp.models.Order
import com.example.tradeapp.damin.repository.OrderRepository
import com.example.tradeapp.damin.util.Result
import com.example.tradeapp.models.toCreateOrderDto
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val repository: OrderRepository
) {

    suspend operator fun invoke(
        order: Order
    ): Result<Unit> {

        return try {

            Log.d(
                "CreateOrderUseCase",
                "order = $order"
            )

            val dto =
                order.toCreateOrderDto()

            Log.d(
                "CreateOrderUseCase",
                "dto = $dto"
            )

            repository.createOrder(dto)

        } catch (e: Exception) {

            Log.e(
                "CreateOrderUseCase",
                "error",
                e
            )

            Result.Error(
                Exception(
                    "Failed to create order: ${e.message}",
                    e
                )
            )
        }
    }
}