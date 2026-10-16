package com.bvic.followmycrypto.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bvic.followmycrypto.data.CryptoRepository
import com.bvic.followmycrypto.data.FavoritesRepository // Évolution 12
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted // Évolution 12
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map // Évolution 12
import kotlinx.coroutines.flow.stateIn // Évolution 12
import kotlinx.coroutines.launch

class CryptoDetailViewModel(
    private val symbol: String,
    private val repository: CryptoRepository,
    private val favoritesRepository: FavoritesRepository, // Évolution 12 : le même repository que la liste
) : ViewModel() {

    private val _uiState = MutableStateFlow<CryptoDetailUiState>(CryptoDetailUiState.Loading)
    val uiState: StateFlow<CryptoDetailUiState> = _uiState.asStateFlow()

    // Évolution 12 : est-ce que CETTE crypto est en favori ? (true / false)
    val isFavorite: StateFlow<Boolean> = favoritesRepository.favorites
        .map { favorites -> symbol in favorites }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    init {
        load()
    }

    fun retry() = load()

    // Évolution 12 : clic sur le cœur
    fun toggleFavorite() = favoritesRepository.toggle(symbol)

    private fun load() {
        _uiState.value = CryptoDetailUiState.Loading
        viewModelScope.launch {
            try {
                _uiState.value = CryptoDetailUiState.Success(repository.getCrypto(symbol))
            } catch (e: Exception) {
                _uiState.value = CryptoDetailUiState.Error("Impossible de charger $symbol.")
            }
        }
    }
}