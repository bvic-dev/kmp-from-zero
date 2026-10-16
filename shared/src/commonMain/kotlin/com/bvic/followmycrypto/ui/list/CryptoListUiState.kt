package com.bvic.followmycrypto.ui.list

import com.bvic.followmycrypto.model.Crypto

// Tout ce que l'écran peut afficher, et rien d'autre
sealed interface CryptoListUiState {
    data object Loading : CryptoListUiState
    data class Error(val message: String) : CryptoListUiState
    data class Success(
        // Les cryptos à afficher (déjà filtrées si le filtre est actif)
        val cryptos: List<Crypto>,
        // Évolution 12 : les symboles en favori, ex. {"BTC", "SOL"}
        val favorites: Set<String> = emptySet(),
        // Évolution 12 : le filtre « favoris seulement » est-il activé ?
        val showOnlyFavorites: Boolean = false,
    ) : CryptoListUiState
}