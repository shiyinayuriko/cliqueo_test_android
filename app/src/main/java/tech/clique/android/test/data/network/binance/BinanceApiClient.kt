package tech.clique.android.test.data.network.binance

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import tech.clique.android.test.data.model.KlineData
import tech.clique.android.test.data.model.KlineDataSource
import tech.clique.android.test.data.Symbol
import tech.clique.android.test.data.model.TickerData
import tech.clique.android.test.data.model.TickerDataSource
import java.lang.reflect.Type


object BinanceApiClient {
    private val api = Retrofit.Builder()
        .baseUrl(BINANCE_BASE_URL)
//        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build().create(BinanceApiService::class.java)

    fun get24HrTickers(): Observable<List<FullTickerItem>> {
        val symbols = "[" + ALL_SYMBOLS.joinToString(",") { symbol -> "\"${symbol.uppercase()}\"" } + "]"
        return api.get24HrTickers(symbols)
            .retry(3)
            .subscribeOn(Schedulers.io())
    }

    fun getKlineData(
        symbol: String,
        interval: String,
        startTime: Long? = null,
        endTime: Long? = null,
        limit: Int = 500,
    ): Observable<List<KlineItem>> {
        return api.getKlines(symbol.uppercase(), interval, startTime, endTime, limit)
            .retry(3)
            .subscribeOn(Schedulers.io())
    }
}


private interface BinanceApiService {
    @GET("/api/v3/ticker/24hr")
    fun get24HrTickers(
        @Query("symbols") symbols: String,
//        @Query("type") type: String = "FULL",
    ): Observable<List<FullTickerItem>>

    @GET("/api/v3/klines")
    fun getKlines(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("startTime") startTime: Long? = null,
        @Query("endTime") endTime: Long? = null,
//        @Query("timeZone") timeZone: Int = 0,
        @Query("limit") limit: Int = 500,    // max 1000
    ): Observable<List<KlineItem>>
}

data class FullTickerItem(
    @SerializedName("symbol") val symbol: String,
    @SerializedName("priceChange") val priceChange: String,
    @SerializedName("priceChangePercent") val priceChangePercent: String,
    @SerializedName("weightedAvgPrice") val weightedAvgPrice: String,
    @SerializedName("prevClosePrice") val prevClosePrice: String,
    @SerializedName("lastPrice") val lastPrice: String,
    @SerializedName("lastQty") val lastQty: String,
    @SerializedName("bidPrice") val bidPrice: String,
    @SerializedName("bidQty") val bidQty: String,
    @SerializedName("askPrice") val askPrice: String,
    @SerializedName("askQty") val askQty: String,
    @SerializedName("openPrice") val openPrice: String,
    @SerializedName("highPrice") val highPrice: String,
    @SerializedName("lowPrice") val lowPrice: String,
    @SerializedName("volume") val volume: String,
    @SerializedName("quoteVolume") val quoteVolume: String,
    @SerializedName("openTime") val openTime: Long,
    @SerializedName("closeTime") val closeTime: Long,
    @SerializedName("firstId") val firstTradeId: Long,
    @SerializedName("lastId") val lastTradeId: Long,
    @SerializedName("count") val tradeCount: Long,
) : TickerDataSource {
    override fun toTickerData(): TickerData {
        return TickerData(
            Symbol.fromSymbol(symbol),
            lastPrice,
            bidPrice,
            askPrice,
            priceChangePercent,
        )
    }
}


@JsonAdapter(KlineItemDeserializer::class)
data class KlineItem(
    val openTime: Long,
    val openPrice: String,
    val highPrice: String,
    val lowPrice: String,
    val closePrice: String,
    val baseAssetVolume: String,
    val closeTime: Long,
    val quoteAssetVolume: String,
    val numberOfTrades: Long,
    val takerBuyBaseAssetVolume: String,
    val takerBuyQuoteAssetVolume: String,
    val ignore: String,
) : KlineDataSource {
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

private class KlineItemDeserializer : JsonDeserializer<KlineItem> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): KlineItem {
        val jsonArray = json.asJsonArray
        return KlineItem(
            jsonArray.get(0).asLong,
            jsonArray.get(1).asString,
            jsonArray.get(2).asString,
            jsonArray.get(3).asString,
            jsonArray.get(4).asString,
            jsonArray.get(5).asString,
            jsonArray.get(6).asLong,
            jsonArray.get(7).asString,
            jsonArray.get(8).asLong,
            jsonArray.get(9).asString,
            jsonArray.get(10).asString,
            jsonArray.get(11).asString,
        )
    }
}