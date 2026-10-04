package com.bvic.followmycrypto.data

import com.bvic.followmycrypto.model.Crypto
import kotlinx.coroutines.delay

private val mockCryptos = listOf(
    Crypto(symbol = "BTC", name = "Bitcoin", price = 85613.50, changePercent = 0.88),
    Crypto(symbol = "ETH", name = "Ethereum", price = 3214.07, changePercent = -1.42),
    Crypto(symbol = "BNB", name = "BNB", price = 642.30, changePercent = 0.15),
    Crypto(symbol = "SOL", name = "Solana", price = 148.92, changePercent = 4.31),
    Crypto(symbol = "XRP", name = "XRP", price = 0.5873, changePercent = -0.64),
    Crypto(symbol = "ADA", name = "Cardano", price = 0.4521, changePercent = 2.07),
    Crypto(symbol = "DOGE", name = "Dogecoin", price = 0.1284, changePercent = -3.95),
    Crypto(symbol = "SHIB", name = "Shiba Inu", price = 0.00001834, changePercent = 1.12),
)

// Un faux repository : il simule un serveur lent, sans réseau
class FakeCryptoRepository : CryptoRepository {

    override suspend fun getCryptos(): List<Crypto> {
        delay(1_500)
        return mockCryptos
    }
}
