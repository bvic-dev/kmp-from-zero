package com.bvic.followmycrypto.ui.list

import com.bvic.followmycrypto.model.Crypto

enum class CryptoSort(val label: String) {
    NAME_ASC("Nom (A-Z)"),
    NAME_DESC("Nom (Z-A)"),
    PRICE_DESC("Prix (décroissant)"),
    PRICE_ASC("Prix (croissant)"),
    CHANGE_DESC("Variation (décroissante)"),
    CHANGE_ASC("Variation (croissante)");

    fun sort(cryptos: List<Crypto>): List<Crypto> {
        return when (this) {
            NAME_ASC -> cryptos.sortedBy { it.name.lowercase() }
            NAME_DESC -> cryptos.sortedByDescending { it.name.lowercase() }
            PRICE_DESC -> cryptos.sortedByDescending { it.price }
            PRICE_ASC -> cryptos.sortedBy { it.price }
            CHANGE_DESC -> cryptos.sortedByDescending { it.changePercent }
            CHANGE_ASC -> cryptos.sortedBy { it.changePercent }
        }
    }
}
