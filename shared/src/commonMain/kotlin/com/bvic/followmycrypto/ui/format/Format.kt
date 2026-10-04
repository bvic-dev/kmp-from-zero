package com.bvic.followmycrypto.ui.format

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToLong

// String.format() n'existe que sur la JVM : en commonMain, on formate à la main.
fun formatNumber(value: Double, decimals: Int, grouping: Boolean = true): String {
    val factor = 10.0.pow(decimals).toLong()
    val rounded = (abs(value) * factor).roundToLong()
    val integerPart = (rounded / factor).toString().let { digits ->
        if (grouping) digits.reversed().chunked(3).joinToString(" ").reversed() else digits
    }
    val sign = if (value < 0 && rounded != 0L) "-" else ""
    if (decimals == 0) return sign + integerPart
    val decimalPart = (rounded % factor).toString().padStart(decimals, '0')
    return "$sign$integerPart,$decimalPart"
}

fun formatPrice(value: Double): String {
    val decimals = when {
        value >= 1.0 -> 2
        value >= 0.01 -> 4
        else -> 8
    }
    return "${formatNumber(value, decimals)} $"
}

fun formatPercent(value: Double): String {
    val sign = if (value > 0) "+" else ""
    return "$sign${formatNumber(value, 2)} %"
}
