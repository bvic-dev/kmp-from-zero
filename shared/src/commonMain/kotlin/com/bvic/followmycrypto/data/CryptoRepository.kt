package com.bvic.followmycrypto.data

import com.bvic.followmycrypto.model.Crypto
import com.bvic.followmycrypto.model.Currency

// Le contrat : « je sais fournir la liste des cryptos ». D'où elles viennent ne regarde que l'implémentation.
interface CryptoRepository {
    suspend fun getCryptos(
        currency: Currency
    ): List<Crypto>

    suspend fun getCrypto(
        symbol: String,
        currency: Currency
    ): Crypto
}
