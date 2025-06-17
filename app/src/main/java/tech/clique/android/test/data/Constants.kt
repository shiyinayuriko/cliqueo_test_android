package tech.clique.android.test.data

import tech.clique.android.test.R

enum class Symbol(
    val baseAsset: String,
    val quoteAsset: String,
    val symbol: String, //binance api
) {
    UNKNOWN("UNKNOWN", "UNKNOWN", "UNKNOWN"),
    ETHBTC("ETH", "BTC", "ETHBTC"),
    LTCBTC("LTC", "BTC", "LTCBTC"),
    BNBBTC("BNB", "BTC", "BNBBTC"),
    NEOBTC("NEO", "BTC", "NEOBTC"),
    QTUMETH("QTUM", "ETH", "QTUMETH"),
    GASBTC("GAS", "BTC", "GASBTC"),
    BNBETH("BNB", "ETH", "BNBETH"),
    BTCUSDT("BTC", "USDT", "BTCUSDT"),
    ETHUSDT("ETH", "USDT", "ETHUSDT"),
    LRCBTC("LRC", "BTC", "LRCBTC"),
    LRCETH("LRC", "ETH", "LRCETH"),
    QTUMBTC("QTUM", "BTC", "QTUMBTC"),
    ZRXBTC("ZRX", "BTC", "ZRXBTC"),
    KNCBTC("KNC", "BTC", "KNCBTC"),
    IOTABTC("IOTA", "BTC", "IOTABTC"),
    IOTAETH("IOTA", "ETH", "IOTAETH"),
    LINKBTC("LINK", "BTC", "LINKBTC"),
    LINKETH("LINK", "ETH", "LINKETH"),
    XVGETH("XVG", "ETH", "XVGETH"),
    MTLBTC("MTL", "BTC", "MTLBTC"),
    ETCETH("ETC", "ETH", "ETCETH"),
    ETCBTC("ETC", "BTC", "ETCBTC"),
    ZECBTC("ZEC", "BTC", "ZECBTC"),
    ZECETH("ZEC", "ETH", "ZECETH"),
    DASHBTC("DASH", "BTC", "DASHBTC"),
    DASHETH("DASH", "ETH", "DASHETH"),
    REQBTC("REQ", "BTC", "REQBTC"),
    TRXBTC("TRX", "BTC", "TRXBTC"),
    TRXETH("TRX", "ETH", "TRXETH"),
    POWRBTC("POWR", "BTC", "POWRBTC"),
    POWRETH("POWR", "ETH", "POWRETH"),
    XRPBTC("XRP", "BTC", "XRPBTC"),
    XRPETH("XRP", "ETH", "XRPETH"),
    ENJBTC("ENJ", "BTC", "ENJBTC"),
    STORJBTC("STORJ", "BTC", "STORJBTC"),
    BNBUSDT("BNB", "USDT", "BNBUSDT"),
    KMDBTC("KMD", "BTC", "KMDBTC"),
    BATBTC("BAT", "BTC", "BATBTC"),
    NEOUSDT("NEO", "USDT", "NEOUSDT"),
    LSKBTC("LSK", "BTC", "LSKBTC"),
    MANABTC("MANA", "BTC", "MANABTC"),
    MANAETH("MANA", "ETH", "MANAETH"),
    ADXBTC("ADX", "BTC", "ADXBTC"),
    ADXETH("ADX", "ETH", "ADXETH"),
    ADABTC("ADA", "BTC", "ADABTC"),
    ADAETH("ADA", "ETH", "ADAETH"),
    XLMBTC("XLM", "BTC", "XLMBTC"),
    XLMETH("XLM", "ETH", "XLMETH"),
    LTCETH("LTC", "ETH", "LTCETH");

    companion object {
        private val cache: Map<String, Symbol> = Symbol.entries.associateBy { it.symbol }
        fun fromSymbol(symbol: String): Symbol {
            return cache[symbol.uppercase()] ?: UNKNOWN
        }
    }
}

enum class KlineInterval(
    val queryStr: String,
    val display: Int,
    val timeMs: Long,
) {
    INTERVAL_1s("1s", R.string.INTERVAL_1s, 1000L),
    INTERVAL_1m("1m", R.string.INTERVAL_1m, 1000L * 60),
    INTERVAL_3m("3m", R.string.INTERVAL_3m, 1000L * 60 * 3),
    INTERVAL_5m("5m", R.string.INTERVAL_5m, 1000L * 60 * 5),
    INTERVAL_15m("15m", R.string.INTERVAL_15m, 1000L * 60 * 15),
    INTERVAL_30m("30m", R.string.INTERVAL_30m, 1000L * 60 * 30),
    INTERVAL_1h("1h", R.string.INTERVAL_1h, 1000L * 60 * 60),
    INTERVAL_2h("2h", R.string.INTERVAL_2h, 1000L * 60 * 60 * 2),
    INTERVAL_4h("4h", R.string.INTERVAL_4h, 1000L * 60 * 60 * 4),
    INTERVAL_6h("6h", R.string.INTERVAL_6h, 1000L * 60 * 60 * 6),
    INTERVAL_8h("8h", R.string.INTERVAL_8h, 1000L * 60 * 60 * 8),
    INTERVAL_12h("12h", R.string.INTERVAL_12h, 1000L * 60 * 60 * 12),
    INTERVAL_1d("1d", R.string.INTERVAL_1d, 1000L * 60 * 60 * 24),
    INTERVAL_3d("3d", R.string.INTERVAL_3d, 1000L * 60 * 60 * 24 * 3),
    INTERVAL_1w("1w", R.string.INTERVAL_1w, 1000L * 60 * 60 * 24 * 7),
    INTERVAL_1M("1M", R.string.INTERVAL_1M, 1000L * 60 * 60 * 24 * 31),
}