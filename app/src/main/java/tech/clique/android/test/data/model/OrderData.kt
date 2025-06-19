package tech.clique.android.test.data.model

import tech.clique.android.test.data.Symbol
import java.util.UUID

data class OrderData(
    val id: String = UUID.randomUUID().toString(),
    val symbol: Symbol,
    val amount: Float,
    val price: Float,
    val isBuy: Boolean,
    val type: OrderType,
)

enum class OrderType {
    ORDER_TYPE_MARKET,
    ORDER_TYPE_LIMIT,
}