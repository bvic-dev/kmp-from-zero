package com.bvic.followmycrypto.data

import com.bvic.followmycrypto.model.Crypto
import kotlinx.coroutines.delay

private val mockCryptos = listOf(
    Crypto(symbol = "BTC", name = "Bitcoin", price = 85613.50, changePercent = 0.88, lowPrice = 84200.0, highPrice = 86500.0),
    Crypto(symbol = "ETH", name = "Ethereum", price = 3214.07, changePercent = -1.42, lowPrice = 3150.0, highPrice = 3280.0),
    Crypto(symbol = "BNB", name = "BNB", price = 642.30, changePercent = 0.15, lowPrice = 635.0, highPrice = 650.0),
    Crypto(symbol = "SOL", name = "Solana", price = 148.92, changePercent = 4.31, lowPrice = 140.0, highPrice = 152.0),
    Crypto(symbol = "XRP", name = "XRP", price = 0.5873, changePercent = -0.64, lowPrice = 0.57, highPrice = 0.60),
    Crypto(symbol = "ADA", name = "Cardano", price = 0.4521, changePercent = 2.07, lowPrice = 0.44, highPrice = 0.47),
    Crypto(symbol = "DOGE", name = "Dogecoin", price = 0.1284, changePercent = -3.95, lowPrice = 0.12, highPrice = 0.135),
    Crypto(symbol = "SHIB", name = "Shiba Inu", price = 0.00001834, changePercent = 1.12, lowPrice = 0.0000175, highPrice = 0.000019),
)

// Un faux repository : il simule un serveur lent, sans réseau
class FakeCryptoRepository : CryptoRepository {

    override suspend fun getCryptos(): List<Crypto> {
        delay(1_500)
        return mockCryptos
    }

    override suspend fun getCrypto(symbol: String): Crypto {
        delay(500)
        return mockCryptos.first { it.symbol == symbol }
    }
}
