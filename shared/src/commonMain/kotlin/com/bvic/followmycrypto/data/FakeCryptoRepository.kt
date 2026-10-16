package com.bvic.followmycrypto.data

import com.bvic.followmycrypto.model.Crypto
import kotlinx.coroutines.delay

private val mockCryptos = listOf(
    Crypto(symbol = "BTC", name = "Bitcoin", price = 85613.50, changePercent = 0.88, high24h = 85666.01, low24h = 84120.00),
    Crypto(symbol = "ETH", name = "Ethereum", price = 3214.07, changePercent = -1.42, high24h = 3290.50, low24h = 3180.20),
    Crypto(symbol = "BNB", name = "BNB", price = 642.30, changePercent = 0.15, high24h = 648.90, low24h = 636.10),
    Crypto(symbol = "SOL", name = "Solana", price = 148.92, changePercent = 4.31, high24h = 151.40, low24h = 141.80),
    Crypto(symbol = "XRP", name = "XRP", price = 0.5873, changePercent = -0.64, high24h = 0.5960, low24h = 0.5810),
    Crypto(symbol = "ADA", name = "Cardano", price = 0.4521, changePercent = 2.07, high24h = 0.4580, low24h = 0.4400),
    Crypto(symbol = "DOGE", name = "Dogecoin", price = 0.1284, changePercent = -3.95, high24h = 0.1350, low24h = 0.1270),
    Crypto(symbol = "SHIB", name = "Shiba Inu", price = 0.00001834, changePercent = 1.12, high24h = 0.00001860, low24h = 0.00001800),
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
