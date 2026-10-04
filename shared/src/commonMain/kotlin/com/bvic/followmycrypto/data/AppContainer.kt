package com.bvic.followmycrypto.data

import com.bvic.followmycrypto.data.remote.BinanceApi
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

// Injection de dépendances « à la main » : un seul endroit crée les objets partagés
object AppContainer {

    private val httpClient by lazy {
        HttpClient {
            // Une réponse 4xx ou 5xx lève une exception au lieu d'être ignorée
            expectSuccess = true
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    // Pour travailler sans réseau, remplace cette ligne par FakeCryptoRepository()
    val cryptoRepository: CryptoRepository by lazy { RemoteCryptoRepository(BinanceApi(httpClient)) }
}
