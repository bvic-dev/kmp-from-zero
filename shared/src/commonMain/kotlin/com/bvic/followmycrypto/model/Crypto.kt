package com.bvic.followmycrypto.model

data class Crypto(
    val symbol: String,
    val name: String,
    val price: Double,
    val changePercent: Double,
    val lowPrice: Double = 0.0,
    val highPrice: Double = 0.0,
) {
    val logoUrl: String
        get() = "https://cdn.jsdelivr.net/gh/spothq/cryptocurrency-icons@master/128/color/${symbol.lowercase()}.png"
}
