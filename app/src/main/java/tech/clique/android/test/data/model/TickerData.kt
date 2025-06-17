package tech.clique.android.test.data.model

import tech.clique.android.test.data.Symbol

data class TickerData(
    val symbol: Symbol,
    val price: String,
    val bidPrice: String,
    val askPrice: String,
    val changePercentage: String,
)

interface TickerDataSource {
    fun toTickerData(): TickerData
}