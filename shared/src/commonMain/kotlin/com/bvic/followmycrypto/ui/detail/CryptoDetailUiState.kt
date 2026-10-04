package com.bvic.followmycrypto.ui.detail

import com.bvic.followmycrypto.model.Crypto

// Tout ce que l'écran peut afficher, et rien d'autre
sealed interface CryptoDetailUiState {
    data object Loading : CryptoDetailUiState
    data class Error(val message: String) : CryptoDetailUiState
    data class Success(val crypto: Crypto) : CryptoDetailUiState
}
