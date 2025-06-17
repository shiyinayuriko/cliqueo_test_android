package tech.clique.android.test.data

import io.reactivex.rxjava3.core.Observable
import tech.clique.android.test.data.model.KlineData
import tech.clique.android.test.data.model.TickerData
import tech.clique.android.test.data.network.NetworkClient

object DataRepository {
    fun fetchTickers(): Observable<List<TickerData>> {
        //can add cache here
        return NetworkClient.fetchTickers()
    }

    fun subscribeTickers(): Observable<TickerData> {
        return NetworkClient.subscribeTickers()
    }

    fun fetchKline(
        symbol: Symbol,
        interval: KlineInterval,
        startTime: Long? = null,
        endTime: Long? = null,
    ): Observable<Map<Long, KlineData>> {
        //can add cache here
        return NetworkClient.fetchKline(symbol, interval, startTime, endTime)
    }

    fun subscribeKline(symbol: Symbol, interval: KlineInterval): Observable<KlineData> {
        return NetworkClient.subscribeKline(symbol, interval)
    }
}