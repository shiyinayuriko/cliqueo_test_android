package tech.clique.android.test.pages

import android.content.Context
import tech.clique.android.test.R
import tech.clique.android.test.data.TickerData
import tech.clique.android.test.data.network.binance.BTCUSDT
import tech.clique.android.test.data.network.binance.ETHUSDT

val TickerData.trimmedPrice: String
    get() {
        return price.toTrimmedPrice()
    }

fun String.toTrimmedPrice(): String {
    return trimEnd('0').run { if (last() == '.') plus("0") else this }
}

fun String.symbolToDisplayName(context: Context): String {
    return when (this.lowercase()) {
        BTCUSDT -> context.getString(R.string.btc_usdt)
        ETHUSDT -> context.getString(R.string.eth_usdt)
        else -> this
    }
}