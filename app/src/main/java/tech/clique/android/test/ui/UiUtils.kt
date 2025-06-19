package tech.clique.android.test.ui

import tech.clique.android.test.data.Symbol
import java.math.BigDecimal

fun String.toTrimmedPrice(): String {
    return trimEnd('0').run { if (last() == '.') plus("0") else this }
}

fun Symbol.toDisplayName(): String {
    return "$baseAsset/$quoteAsset"
}

fun Float.toFullString(): String {
    return BigDecimal(toString())
        .stripTrailingZeros()
        .toPlainString()
        .removeSuffix(".0")
        .removeSuffix(".")
}