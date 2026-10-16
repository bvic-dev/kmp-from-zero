package com.bvic.followmycrypto.data

import com.bvic.followmycrypto.model.Crypto
import com.bvic.followmycrypto.model.Currency
import kotlinx.coroutines.delay

private val mockCryptos = listOf(
    Crypto(symbol = "BTC", name = "Bitcoin", price = 85613.50,highPrice = 85666.50, lowPrice = 10000.00, changePercent = 0.88),
    Crypto(symbol = "ETH", name = "Ethereum", price = 3214.07,highPrice = 85666.50, lowPrice = 10000.00, changePercent = -1.42),
    Crypto(symbol = "BNB", name = "BNB", price = 642.30,highPrice = 85666.50, lowPrice = 10000.00, changePercent = 0.15),
    Crypto(symbol = "SOL", name = "Solana", price = 148.92,highPrice = 85666.50, lowPrice = 10000.00, changePercent = 4.31),
    Crypto(symbol = "XRP", name = "XRP", price = 0.5873,highPrice = 85666.50, lowPrice = 10000.00, changePercent = -0.64),
    Crypto(symbol = "ADA", name = "Cardano", price = 0.4521,highPrice = 85666.50, lowPrice = 10000.00, changePercent = 2.07),
    Crypto(symbol = "DOGE", name = "Dogecoin", price = 0.1284,highPrice = 85666.50, lowPrice = 10000.00, changePercent = -3.95),
    Crypto(symbol = "SHIB", name = "Shiba Inu", price = 0.00001834,highPrice = 85666.50, lowPrice = 10000.00, changePercent = 1.12),
)

private val mockCryptosEur = listOf(
    Crypto(symbol = "BTC", name = "Bitcoin", price = 73412.06, highPrice = 74000.00, lowPrice = 72000.00, changePercent = 0.84),
    Crypto(symbol = "ETH", name = "Ethereum", price = 2756.31, highPrice = 2800.00, lowPrice = 2700.00, changePercent = -1.45),
    Crypto(symbol = "BNB", name = "BNB", price = 550.20, highPrice = 560.00, lowPrice = 540.00, changePercent = 0.15),
    Crypto(symbol = "SOL", name = "Solana", price = 127.70, highPrice = 130.00, lowPrice = 125.00, changePercent = 4.27),
    Crypto(symbol = "XRP", name = "XRP", price = 0.50, highPrice = 0.52, lowPrice = 0.48, changePercent = -0.64),
    Crypto(symbol = "ADA", name = "Cardano", price = 0.39, highPrice = 0.41, lowPrice = 0.37, changePercent = 2.07),
    Crypto(symbol = "DOGE", name = "Dogecoin", price = 0.11, highPrice = 0.12, lowPrice = 0.10, changePercent = -3.95),
    Crypto(symbol = "SHIB", name = "Shiba Inu", price = 0.00001572, highPrice = 0.00001600, lowPrice = 0.00001500, changePercent = 1.12),
)

// Un faux repository : il simule un serveur lent, sans réseau
class FakeCryptoRepository : CryptoRepository {

    override suspend fun getCryptos(currency: Currency): List<Crypto> {
        delay(1_500)

        return when (currency) {
            Currency.USD -> mockCryptos
            Currency.EUR -> mockCryptosEur
        }
    }

    override suspend fun getCrypto(
        symbol: String,
        currency: Currency
    ): Crypto {
        delay(500)

        val cryptos = when (currency) {
            Currency.USD -> mockCryptos
            Currency.EUR -> mockCryptosEur
        }

        return cryptos.first { it.symbol == symbol }
    }

}
