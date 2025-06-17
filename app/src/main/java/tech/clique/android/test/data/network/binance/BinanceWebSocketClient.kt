package tech.clique.android.test.data.network.binance

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Request.Builder
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import tech.clique.android.test.data.model.KlineData
import tech.clique.android.test.data.model.KlineDataSource
import tech.clique.android.test.data.Symbol
import tech.clique.android.test.data.model.TickerData
import tech.clique.android.test.data.model.TickerDataSource
import tech.clique.android.test.utils.GsonUtil
import tech.clique.android.test.utils.logD
import tech.clique.android.test.utils.logE
import java.lang.reflect.Type
import java.util.UUID


object BinanceWebSocketClient {
    data class FullTickerModel(
        @SerializedName("e") val eventType: String,
        @SerializedName("E") val eventTime: Long,
        @SerializedName("s") val symbol: String,
        @SerializedName("p") val priceChange: String,
        @SerializedName("P") val priceChangePercent: String,
        @SerializedName("w") val weightedAveragePrice: String,
        @SerializedName("x") val firstTradePriceBefore24hr: String,
        @SerializedName("c") val lastPrice: String,
        @SerializedName("Q") val lastQuantity: String,
        @SerializedName("b") val bestBidPrice: String,
        @SerializedName("B") val bestBidQuantity: String,
        @SerializedName("a") val bestAskPrice: String,
        @SerializedName("A") val bestAskQuantity: String,
        @SerializedName("o") val openPrice: String,
        @SerializedName("h") val highPrice: String,
        @SerializedName("l") val lowPrice: String,
        @SerializedName("v") val totalTradedBaseAssetVolume: String,
        @SerializedName("q") val totalTradedQuoteAssetVolume: String,
        @SerializedName("O") val statisticsOpenTime: Long,
        @SerializedName("C") val statisticsCloseTime: Long,
        @SerializedName("F") val firstTradeId: Long,
        @SerializedName("L") val lastTradeId: Long,
        @SerializedName("n") val totalNumberOfTrades: Long,
    ) : BaseModel(), TickerDataSource {
        override fun toTickerData(): TickerData {
            return TickerData(
                Symbol.fromSymbol(symbol),
                lastPrice,
                bestBidPrice,
                bestAskPrice,
                priceChangePercent)
        }
    }


    fun subscribeTickersData(): Observable<FullTickerModel> {
        return Observable.create { emitter ->
            val client = OkHttpClient()
            val request: Request = Builder().url("wss://data-stream.binance.vision/ws").build()
            val ws = client.newWebSocket(request, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    val requestModel = RequestModel(
                        method = "SUBSCRIBE",
                        params = ALL_SYMBOLS.map { symbol -> "${symbol.lowercase()}@ticker" }
                    )
                    GsonUtil.toJson(requestModel)?.also {
                        webSocket.send(it)
                    } ?: logE("subscribeTickersData onOpen requestModel is null")
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    logE("subscribeTickersData onFail ${t.message}", t)
                    if (!emitter.isDisposed) {
                        emitter.onError(t)
                    }
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    logD("subscribeTickersData onMessage: $text")
                    val fullTicker = GsonUtil.fromJson(text, BaseModel::class.java)
                    logD("fullTicker $fullTicker")
                    if (fullTicker is FullTickerModel) {
                        emitter.onNext(fullTicker)
                    }
                }
            })
            emitter.setCancellable { ws.close(1000, "Rx Cancelled") }
        }
            .retry(3)
            .subscribeOn(Schedulers.io())
    }

    data class AggTradeModel(
        @SerializedName("e") val eventType: String,
        @SerializedName("E") val eventTime: Long,
        @SerializedName("s") val symbol: String,
        @SerializedName("a") val aggregateTradeId: Long,
        @SerializedName("p") val price: String,
        @SerializedName("q") val quantity: String,
        @SerializedName("f") val firstTradeId: Long,
        @SerializedName("l") val lastTradeId: Long,
        @SerializedName("T") val tradeTime: Long,
        @SerializedName("m") val isTheBuyerTheMarketMaker: Boolean,
        @SerializedName("M") val ignore: Boolean,
    ) : BaseModel()

    fun subscribeAggTradesData(): Observable<AggTradeModel> {
        return Observable.create { emitter ->
            val client = OkHttpClient()
            val request: Request = Builder().url("wss://data-stream.binance.vision/ws").build()
            val ws = client.newWebSocket(request, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    val requestModel = RequestModel(
                        method = "SUBSCRIBE",
                        params = ALL_SYMBOLS.map { symbol -> "${symbol.lowercase()}@aggTrade" }
                    )
                    GsonUtil.toJson(requestModel)?.also {
                        webSocket.send(it)
                    } ?: logE("subscribeAggTradesData onOpen requestModel is null")
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    logE("subscribeAggTradesData onFail ${t.message}", t)
                    if (!emitter.isDisposed) {
                        emitter.onError(t)
                    }
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    logD("subscribeAggTradesData onMessage: $text")
                    val aggTradeModel = GsonUtil.fromJson(text, BaseModel::class.java)
                    logD("tradeEvent $aggTradeModel")
                    if (aggTradeModel is AggTradeModel) {
                        emitter.onNext(aggTradeModel)
                    }
                }
            })
            emitter.setCancellable { ws.close(1000, "Rx Cancelled") }
        }
            .retry(3)
            .subscribeOn(Schedulers.io())
    }

    data class KlineModel(
        @SerializedName("e") val eventType: String,
        @SerializedName("E") val eventTime: Long,
        @SerializedName("s") val symbol: String,
        @SerializedName("k") val detail: KlineDetailModel,
    ) : BaseModel()

    data class KlineDetailModel(
        @SerializedName("t") val openTime: Long,
        @SerializedName("T") val closeTime: Long,
        @SerializedName("s") val symbol: String,
        @SerializedName("i") val interval: String,
        @SerializedName("f") val firstTradeId: Long,
        @SerializedName("L") val lastTradeId: Long,
        @SerializedName("o") val openPrice: String,
        @SerializedName("c") val closePrice: String,
        @SerializedName("h") val highPrice: String,
        @SerializedName("l") val lowPrice: String,
        @SerializedName("x") val isClosed: Boolean,
        @SerializedName("n") val numberOfTrades: Long,
        @SerializedName("v") val baseAssetVolume: String,
        @SerializedName("q") val quoteAssetVolume: String,
        @SerializedName("V") val takerBuyBaseAssetVolume: String,
        @SerializedName("Q") val takerBuyQuoteAssetVolume: String,
        @SerializedName("B") val ignore: String,
    ) : BaseModel(), KlineDataSource {
        override fun toKlineData(): KlineData {
            return KlineData(
                openTime = openTime,
                closeTime = closeTime,
                openPrice = openPrice,
                closePrice = closePrice,
                highPrice = highPrice,
                lowPrice = lowPrice,
                baseAssetVolume = baseAssetVolume,
            )
        }
    }

    fun subscribeKlineData(symbol: String, interval: String): Observable<KlineModel> {
        return Observable.create { emitter ->
            val client = OkHttpClient()
            val request: Request = Builder().url("wss://data-stream.binance.vision/ws").build()
            val ws = client.newWebSocket(request, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    val requestModel = RequestModel(
                        method = "SUBSCRIBE",
                        params = listOf(
                            "${symbol.lowercase()}@kline_$interval"
                        )
                    )
                    GsonUtil.toJson(requestModel)?.also {
                        webSocket.send(it)
                    } ?: logE("subscribeKlineData onOpen requestModel is null")
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    logE("subscribeKlineData onFail ${t.message}", t)
                    if (!emitter.isDisposed) {
                        emitter.onError(t)
                    }
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    logD("subscribeKlineData onMessage: $text")
                    val klineModel = GsonUtil.fromJson(text, BaseModel::class.java)
                    logD("klineModel $klineModel")
                    if (klineModel is KlineModel) {
                        emitter.onNext(klineModel)
                    }
                }
            })
            emitter.setCancellable { ws.close(1000, "Rx Cancelled") }
        }
            .retry(3)
            .subscribeOn(Schedulers.io())
    }

    private data class RequestModel(
        val method: String = "SUBSCRIBE",
        val params: List<String> = emptyList(),
        val id: String = UUID.randomUUID().toString()
    )

    private data class ResultModel(
        @SerializedName("result") val result: String?,
        @SerializedName("id") val id: String,
    ) : BaseModel()

    @JsonAdapter(WebSocketModelDeserializer::class)
    abstract class BaseModel()
    private class WebSocketModelDeserializer : JsonDeserializer<BaseModel> {
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): BaseModel {
            val jsonObject = json.asJsonObject

            return when (jsonObject.get("e")?.asString) {
                "kline" -> {
                    context.deserialize(json, KlineModel::class.java)
                }

                "24hrTicker" -> {
                    context.deserialize(json, FullTickerModel::class.java)
                }

                "aggTrade" -> {
                    context.deserialize(json, AggTradeModel::class.java)
                }

                else -> context.deserialize(json, ResultModel::class.java)
            }
        }
    }
}