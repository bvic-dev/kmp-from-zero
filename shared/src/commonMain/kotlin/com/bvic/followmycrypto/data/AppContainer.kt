
package com.bvic.followmycrypto.data

import com.bvic.followmycrypto.data.remote.BinanceApi
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object AppContainer {

    private val httpClient by lazy {
        HttpClient {
            expectSuccess = true
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    // Pour travailler sans réseau, remplace cette ligne par FakeCryptoRepository()
    val cryptoRepository: CryptoRepository by lazy { RemoteCryptoRepository(BinanceApi(httpClient)) }

    val favoriteRepository: FavoriteRepository by lazy {
        FavoriteRepository()
    }
}
