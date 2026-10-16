package com.bvic.followmycrypto.model

data class Crypto(
    val symbol: String,
    val name: String,
    val price: Double,
    val highPrice: Double,
    val lowPrice: Double,
    val changePercent: Double,
){
    val logoUrl: String
        get() = "https://cdn.jsdelivr.net/gh/spothq/cryptocurrency-icons@master/128/color/${symbol.lowercase()}.png"

}
