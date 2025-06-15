package tech.clique.android.test.data

import io.reactivex.rxjava3.core.Observable
import tech.clique.android.test.data.network.NetworkClient

object DataRepository {
    fun fetchTickers(): Observable<List<TickerData>> {
        //can add cache here
        return NetworkClient.fetchTickers()
    }
    fun subscribeTickers(): Observable<TickerData> {
        return NetworkClient.subscribeTickers()
    }
}