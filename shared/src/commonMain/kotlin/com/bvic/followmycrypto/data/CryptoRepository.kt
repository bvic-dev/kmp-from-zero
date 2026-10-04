package com.bvic.followmycrypto.data

import com.bvic.followmycrypto.model.Crypto

// Le contrat : « je sais fournir la liste des cryptos ». D'où elles viennent ne regarde que l'implémentation.
interface CryptoRepository {
    suspend fun getCryptos(): List<Crypto>

    suspend fun getCrypto(symbol: String): Crypto
}
