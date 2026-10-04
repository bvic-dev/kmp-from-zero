package com.bvic.followmycrypto.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class BinanceApi(private val client: HttpClient) {

    // pairs = ["BTCUSDT", "ETHUSDT"] devient le paramètre symbols=["BTCUSDT","ETHUSDT"]
    suspend fun getTickers(pairs: List<String>): List<TickerDto> =
        client.get("$BASE_URL/api/v3/ticker/24hr") {
            parameter("symbols", pairs.joinToString(separator = ",", prefix = "[", postfix = "]") { "\"$it\"" })
        }.body()

    private companion object {
        // Domaine de Binance dédié aux données publiques de marché, sans clé d'API
        const val BASE_URL = "https://data-api.binance.vision"
    }
}
