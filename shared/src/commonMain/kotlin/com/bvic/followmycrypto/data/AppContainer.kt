package com.bvic.followmycrypto.data

// Injection de dépendances « à la main » : un seul endroit crée les objets partagés
object AppContainer {

    val cryptoRepository: CryptoRepository by lazy { FakeCryptoRepository() }
}
