package com.bvic.followmycrypto.ui.list

import com.bvic.followmycrypto.model.Crypto

// Tout ce que l'écran peut afficher, et rien d'autre
sealed interface CryptoListUiState {
    data object Loading : CryptoListUiState
    data class Error(val message: String) : CryptoListUiState
    data class Success(
        val cryptos: List<Crypto>,
        // L’écran utilise ces choix pour afficher le bouton actif et le texte saisi.
        val sort: CryptoSort = CryptoSort.NAME,
        val query: String = "",
    ) : CryptoListUiState
}
