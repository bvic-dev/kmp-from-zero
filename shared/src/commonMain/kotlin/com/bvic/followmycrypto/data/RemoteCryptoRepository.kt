package com.bvic.followmycrypto.data

import com.bvic.followmycrypto.data.remote.BinanceApi
import com.bvic.followmycrypto.data.remote.TickerDto
import com.bvic.followmycrypto.model.Crypto
import com.bvic.followmycrypto.model.Devise
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json

private const val SETTINGS_KEY = "base_crypto_names"

private val defaultCryptoNames = mapOf(
    "BTC" to "Bitcoin",
    "ETH" to "Ethereum",
    "BNB" to "BNB",
    "SOL" to "Solana",
    "XRP" to "XRP",
    "ADA" to "Cardano",
    "DOGE" to "Dogecoin",
    "SHIB" to "Shiba Inu",
)

class RemoteCryptoRepository(
    private val api: BinanceApi,
    private val settings: Settings = Settings(),
) : CryptoRepository {

    private val baseCryptoNames: MutableMap<String, String> = loadCryptoNames().toMutableMap()

    private fun loadCryptoNames(): Map<String, String> {
        val savedJson = settings.getStringOrNull(SETTINGS_KEY)
        if (savedJson.isNullOrBlank()) {
            saveCryptoNames(defaultCryptoNames)
            return defaultCryptoNames
        }
        return try {
            Json.decodeFromString<Map<String, String>>(savedJson)
        } catch (e: Exception) {
            defaultCryptoNames
        }
    }

    private fun saveCryptoNames(names: Map<String, String>) {
        val json = Json.encodeToString(names)
        settings.putString(SETTINGS_KEY, json)
    }

    override fun addCrypto(symbol: String, name: String) {
        val uppercaseSymbol = symbol.trim().uppercase()
        val trimmedName = name.trim()
        if (uppercaseSymbol.isNotEmpty() && trimmedName.isNotEmpty()) {
            baseCryptoNames[uppercaseSymbol] = trimmedName
            saveCryptoNames(baseCryptoNames)
        }
    }

    override fun removeCrypto(symbol: String) {
        val uppercaseSymbol = symbol.trim().uppercase()
        if (uppercaseSymbol.isNotEmpty()) {
            baseCryptoNames.remove(uppercaseSymbol)
            saveCryptoNames(baseCryptoNames)
        }
    }

    override suspend fun getCryptos(devise: Devise): List<Crypto> {
        // 1. Les paires à demander
        val pairs = baseCryptoNames.keys.map { "$it${devise.code}" }
        return try {
            // 2. L'appel à l'API
            val tickers = api.getTickers(pairs)
            // 3. La conversion de chaque DTO en modèle
            tickers.map { ticker -> ticker.toCrypto(devise) }
        } catch (e: Exception) {
            baseCryptoNames.map { (symbol, name) ->
                try {
                    val pair = "$symbol${devise.code}"
                    api.getTickers(listOf(pair)).first().toCrypto(devise)
                } catch (singleError: Exception) {
                    Crypto(
                        symbol = symbol,
                        name = "$name (Erreur)",
                        price = 0.0,
                        changePercent = 0.0,
                        highPrice = 0.0
                    )
                }
            }
        }
    }

    override suspend fun getCrypto(symbol: String, devise: Devise): Crypto {
        return try {
            val pair = "$symbol${devise.code}"
            val tickers = api.getTickers(listOf(pair))
            tickers.first().toCrypto(devise)
        } catch (e: Exception) {
            Crypto(
                symbol = symbol,
                name = "${baseCryptoNames[symbol] ?: symbol} (Erreur)",
                price = 0.0,
                changePercent = 0.0,
                highPrice = 0.0
            )
        }
    }

    private fun TickerDto.toCrypto(devise: Devise): Crypto {
        val baseSymbol = symbol.removeSuffix(devise.code)
        return Crypto(
            symbol = baseSymbol,
            name = baseCryptoNames[baseSymbol] ?: baseSymbol,
            price = lastPrice.toDouble(),
            changePercent = priceChangePercent.toDouble(),
            highPrice = highPrice.toDouble()
        )
    }
}
