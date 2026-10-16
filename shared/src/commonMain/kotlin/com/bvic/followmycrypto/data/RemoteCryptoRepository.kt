package com.bvic.followmycrypto.data

import com.bvic.followmycrypto.data.remote.BinanceApi
import com.bvic.followmycrypto.data.remote.TickerDto
import com.bvic.followmycrypto.model.Crypto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

// Les cryptos suivies : la paire à demander à Binance, et le nom à afficher (Binance ne le fournit pas)
private val cryptoNames = mapOf(
    "BTCUSDT" to "Bitcoin",
    "ETHUSDT" to "Ethereum",
    "BNBUSDT" to "BNB",
    "SOLUSDT" to "Solana",
    "XRPUSDT" to "XRP",
    "ADAUSDT" to "Cardano",
    "DOGEUSDT" to "Dogecoin",
    "SHIBUSDT" to "Shiba Inu",
)

class RemoteCryptoRepository(private val api: BinanceApi) : CryptoRepository {

    override suspend fun getCryptos(): List<Crypto> = withContext(Dispatchers.IO) {
        // 1. Les paires à demander
        val pairs = cryptoNames.keys.toList()
        // 2. L'appel à l'API
        val tickers = api.getTickers(pairs)
        // 3. La conversion de chaque DTO en modèle
        tickers.map { ticker -> ticker.toCrypto() }
    }

    override suspend fun getCrypto(symbol: String): Crypto = withContext(Dispatchers.IO) {
        val tickers = api.getTickers(listOf(symbol + "USDT"))
        tickers.first().toCrypto()
    }
}

// DTO (ce que l'API envoie) → modèle (ce dont l'application a besoin)
private fun TickerDto.toCrypto(): Crypto {
    return Crypto(
        symbol = symbol.removeSuffix("USDT"),
        name = cryptoNames[symbol] ?: symbol,
        price = lastPrice.toDouble(),
        highPrice = highPrice.toDouble(),
        lowPrice = lowPrice.toDouble(),
        changePercent = priceChangePercent.toDouble(),
    )
}
