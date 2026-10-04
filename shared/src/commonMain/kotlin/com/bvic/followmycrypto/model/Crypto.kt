package com.bvic.followmycrypto.model

data class Crypto(
    val symbol: String,
    val name: String,
    val price: Double,
    val changePercent: Double,
)
