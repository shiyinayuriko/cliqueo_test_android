package tech.clique.android.test.data.model

import tech.clique.android.test.data.network.binance.Symbol

data class TickerData(
    @Symbol val symbol: String,
    val price: String,
    val changePercentage: String,
)

interface TickerDataSource {
    fun toTickerData(): TickerData
}