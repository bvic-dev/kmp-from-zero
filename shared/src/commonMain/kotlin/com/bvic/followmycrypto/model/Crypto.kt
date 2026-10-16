package com.bvic.followmycrypto.model

data class Crypto(
    val symbol: String,
    val name: String,
    val price: Double,
    val changePercent: Double,
    val high24h: Double,
    val low24h: Double,
) {
    // L'adresse du logo se déduit du symbole : un seul endroit sait comment la construire
    val logoUrl: String
        get() = "https://cdn.jsdelivr.net/gh/spothq/cryptocurrency-icons@master/128/color/${symbol.lowercase()}.png"
}