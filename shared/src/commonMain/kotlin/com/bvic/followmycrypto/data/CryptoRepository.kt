package com.bvic.followmycrypto.data

import com.bvic.followmycrypto.model.*

// Le contrat : « je sais fournir la liste des cryptos ». D'où elles viennent ne regarde que l'implémentation.
interface CryptoRepository {
    suspend fun getCryptos(devise: Devise = Devise.USDT): List<Crypto>

    suspend fun getCrypto(symbol: String, devise: Devise = Devise.USDT): Crypto

    fun addCrypto(symbol: String, name: String)
}
