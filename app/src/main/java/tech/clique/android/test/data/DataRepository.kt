package tech.clique.android.test.data

import io.reactivex.rxjava3.core.Observable
import tech.clique.android.test.data.model.KlineData
import tech.clique.android.test.data.model.TickerData
import tech.clique.android.test.data.network.NetworkClient
import tech.clique.android.test.data.network.binance.KlineInterval
import tech.clique.android.test.data.network.binance.Symbol

object DataRepository {
    fun fetchTickers(): Observable<List<TickerData>> {
        //can add cache here
        return NetworkClient.fetchTickers()
    }

    fun subscribeTickers(): Observable<TickerData> {
        return NetworkClient.subscribeTickers()
    }

    fun fetchKline(
        @Symbol symbol: String,
        @KlineInterval interval: String,
        startTime: Long? = null,
        endTime: Long? = null,
    ): Observable<Map<Long, KlineData>> {
        //can add cache here
        return NetworkClient.fetchKline(symbol, interval, startTime, endTime)
    }

    fun subscribeKline(@Symbol symbol: String, @KlineInterval interval: String): Observable<KlineData> {
        return NetworkClient.subscribeKline(symbol, interval)
    }
}