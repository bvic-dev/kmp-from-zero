package com.bvic.followmycrypto.model

enum class Currency(
    val code: String,
    val symbol: String
) {
    USD("USDT", "$"),
    EUR("EUR", "€")
}