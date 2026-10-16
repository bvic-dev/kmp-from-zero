package com.bvic.followmycrypto.model

enum class Devise(val code: String, val symbol: String) {
    USDT("USDT", "$"),
    EUR("EUR", "€"),
    JPY("JPY", "¥")
}
