package tech.clique.android.test.data

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