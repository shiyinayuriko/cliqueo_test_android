package tech.clique.android.test.data.model


data class KlineData(
    val openTime: Long,
    val closeTime: Long,
    val openPrice: String,
    val closePrice: String,
    val highPrice: String,
    val lowPrice: String,
    val baseAssetVolume: String,
)


interface KlineDataSource {
    fun toKlineData(): KlineData
}
