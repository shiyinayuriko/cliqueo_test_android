package tech.clique.android.test.data.network.binance

import androidx.annotation.StringDef
import tech.clique.android.test.data.Symbol

val Symbol.binanceSymbol: String
    get() = symbol

val ALL_SYMBOLS = Symbol.entries
    .filter { symbol: Symbol -> symbol != Symbol.UNKNOWN }
    .map { symbol: Symbol -> symbol.binanceSymbol }


const val BINANCE_BASE_URL = "https://api4.binance.com"

const val INTERVAL_1s = "1s"
const val INTERVAL_1m = "1m"
const val INTERVAL_3m = "3m"
const val INTERVAL_5m = "5m"
const val INTERVAL_15m = "15m"
const val INTERVAL_30m = "30m"
const val INTERVAL_1h = "1h"
const val INTERVAL_2h = "2h"
const val INTERVAL_4h = "4h"
const val INTERVAL_6h = "6h"
const val INTERVAL_8h = "8h"
const val INTERVAL_12h = "12h"
const val INTERVAL_1d = "1d"
const val INTERVAL_3d = "3d"
const val INTERVAL_1w = "1w"
const val INTERVAL_1M = "1M"

@StringDef(
    INTERVAL_1s,
    INTERVAL_1m,
    INTERVAL_3m,
    INTERVAL_5m,
    INTERVAL_15m,
    INTERVAL_30m,
    INTERVAL_1h,
    INTERVAL_2h,
    INTERVAL_4h,
    INTERVAL_6h,
    INTERVAL_8h,
    INTERVAL_12h,
    INTERVAL_1d,
    INTERVAL_3d,
    INTERVAL_1w,
    INTERVAL_1M,
)
@Retention(AnnotationRetention.SOURCE)
annotation class KlineInterval
