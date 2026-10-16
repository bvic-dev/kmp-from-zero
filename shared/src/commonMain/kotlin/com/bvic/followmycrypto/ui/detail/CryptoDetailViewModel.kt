
package com.bvic.followmycrypto.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bvic.followmycrypto.data.CryptoRepository
import com.bvic.followmycrypto.data.FavoriteRepository
import com.bvic.followmycrypto.model.Currency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CryptoDetailViewModel(
    private val symbol: String,
    private val repository: CryptoRepository,
    private val favoriteRepository: FavoriteRepository,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<CryptoDetailUiState>(
            CryptoDetailUiState.Loading
        )

    val uiState: StateFlow<CryptoDetailUiState> =
        _uiState.asStateFlow()

    val favorites: StateFlow<Set<String>> =
        favoriteRepository.favorites

    fun toggleFavorite() {
        favoriteRepository.toggleFavorite(symbol)
    }

    fun retry() = load()

    init {
        load()
    }

    private fun load() {
        _uiState.value = CryptoDetailUiState.Loading
        viewModelScope.launch {
            try {
                _uiState.value = CryptoDetailUiState.Success(repository.getCrypto(symbol, Currency.USD))
            } catch (e: Exception) {
                _uiState.value = CryptoDetailUiState.Error("Impossible de charger $symbol.")
            }
        }
    }
}
