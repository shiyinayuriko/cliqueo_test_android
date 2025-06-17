package tech.clique.android.test.data.network

import io.reactivex.rxjava3.core.Observable
import tech.clique.android.test.data.model.KlineData
import tech.clique.android.test.data.model.TickerData
import tech.clique.android.test.data.network.binance.BinanceApiClient
import tech.clique.android.test.data.network.binance.BinanceWebSocketClient
import tech.clique.android.test.data.network.binance.KlineInterval
import tech.clique.android.test.data.network.binance.Symbol

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
        @Symbol symbol: String,
        @KlineInterval interval: String,
        startTime: Long? = null,
        endTime: Long? = null,
    ): Observable<Map<Long, KlineData>> {
        return BinanceApiClient.getKlineData(symbol, interval, startTime, endTime).map { list ->
            list.associate {
                val kLineData = it.toKlineData()
                kLineData.openTime to kLineData
            }
        }
    }

    fun subscribeKline(@Symbol symbol: String, @KlineInterval interval: String): Observable<KlineData> {
        return BinanceWebSocketClient.subscribeKlineData(symbol, interval)
            .map { it.detail.toKlineData() }
    }
}