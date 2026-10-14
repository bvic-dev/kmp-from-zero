package com.bvic.followmycrypto.ui.list

import com.bvic.followmycrypto.model.*

// Tout ce que l'écran peut afficher, et rien d'autre
sealed interface CryptoListUiState {
    data object Loading : CryptoListUiState
    data class Error(val message: String) : CryptoListUiState
    data class Success(
        val cryptos: List<Crypto>,
        val selectedSort: CryptoSort = CryptoSort.NAME_ASC,
        val selectedDevise: Devise = Devise.USDT,
    ) : CryptoListUiState
}
