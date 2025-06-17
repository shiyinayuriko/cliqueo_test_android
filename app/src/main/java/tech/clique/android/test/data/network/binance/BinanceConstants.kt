package tech.clique.android.test.data.network.binance

import tech.clique.android.test.data.KlineInterval
import tech.clique.android.test.data.Symbol

const val BINANCE_BASE_URL = "https://api4.binance.com"

val Symbol.binanceSymbol: String
    get() = symbol

val ALL_SYMBOLS = Symbol.entries
    .filter { symbol: Symbol -> symbol != Symbol.UNKNOWN }
    .map { symbol: Symbol -> symbol.binanceSymbol }

val KlineInterval.binanceQueryStr: String
    get() = queryStr
