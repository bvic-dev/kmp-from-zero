package com.bvic.followmycrypto.data

import com.bvic.followmycrypto.data.remote.BinanceApi
import com.bvic.followmycrypto.data.remote.TickerDto
import com.bvic.followmycrypto.model.Crypto

// Binance ne fournit pas le nom des cryptos : on garde notre propre liste
private val cryptoNames = mapOf(
    "BTC" to "Bitcoin",
    "ETH" to "Ethereum",
    "BNB" to "BNB",
    "SOL" to "Solana",
    "XRP" to "XRP",
    "ADA" to "Cardano",
    "DOGE" to "Dogecoin",
    "SHIB" to "Shiba Inu",
)

private const val QUOTE = "USDT"

class RemoteCryptoRepository(private val api: BinanceApi) : CryptoRepository {

    override suspend fun getCryptos(): List<Crypto> {
        val tickers = api.getTickers(cryptoNames.keys.map { it + QUOTE }).associateBy { it.symbol }
        // Binance ne garantit pas l'ordre de la réponse : on garde celui de notre liste
        return cryptoNames.keys.mapNotNull { symbol -> tickers[symbol + QUOTE]?.toCrypto() }
    }

    override suspend fun getCrypto(symbol: String): Crypto =
        api.getTickers(listOf(symbol + QUOTE)).first().toCrypto()
}

// DTO (ce que l'API envoie) → modèle (ce dont l'application a besoin)
private fun TickerDto.toCrypto(): Crypto {
    val symbol = symbol.removeSuffix(QUOTE)
    return Crypto(
        symbol = symbol,
        name = cryptoNames[symbol] ?: symbol,
        price = lastPrice.toDouble(),
        changePercent = priceChangePercent.toDouble(),
    )
}
