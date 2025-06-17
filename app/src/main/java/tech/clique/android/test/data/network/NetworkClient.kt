package tech.clique.android.test.data.network

import io.reactivex.rxjava3.core.Observable
import tech.clique.android.test.data.model.KlineData
import tech.clique.android.test.data.model.TickerData
import tech.clique.android.test.data.network.binance.BinanceApiClient
import tech.clique.android.test.data.network.binance.BinanceWebSocketClient
import tech.clique.android.test.data.Symbol
import tech.clique.android.test.data.KlineInterval
import tech.clique.android.test.data.network.binance.binanceQueryStr
import tech.clique.android.test.data.network.binance.binanceSymbol

object NetworkClient {
    fun fetchTickers(): Observable<List<TickerData>> {
        return BinanceApiClient.get24HrTickers()
            .map { list ->
                list.map { it.toTickerData() }
            }
    }

    fun subscribeTickers(): Observable<TickerData> {
        return BinanceWebSocketClient.subscribeTickersData()
            .map {
                it.toTickerData()
            }
    }

    fun fetchKline(
        symbol: Symbol,
        interval: KlineInterval,
        startTime: Long? = null,
        endTime: Long? = null,
    ): Observable<Map<Long, KlineData>> {
        return BinanceApiClient.getKlineData(symbol.binanceSymbol, interval.binanceQueryStr, startTime, endTime).map { list ->
            list.associate {
                val kLineData = it.toKlineData()
                kLineData.openTime to kLineData
            }
        }
    }

    fun subscribeKline(symbol: Symbol, interval: KlineInterval): Observable<KlineData> {
        return BinanceWebSocketClient.subscribeKlineData(symbol.binanceSymbol, interval.binanceQueryStr)
            .map { it.detail.toKlineData() }
    }
}