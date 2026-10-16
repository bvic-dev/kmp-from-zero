package com.bvic.followmycrypto.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bvic.followmycrypto.data.CryptoRepository
import com.bvic.followmycrypto.data.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CryptoListViewModel(
    private val repository: CryptoRepository,
    private val favoritesRepository: FavoritesRepository, // (évolution 12)
) : ViewModel() {

    // Version modifiable, privée : seul le ViewModel peut changer l'état
    private val _uiState = MutableStateFlow<CryptoListUiState>(CryptoListUiState.Loading)

    // évolution 12, le filtre « favoris seulement » (false = désactivé au départ)
    private val _showOnlyFavorites = MutableStateFlow(false)

    // Version en lecture seule, exposée à l'interface
    // ← MODIFIÉ : on mélange le chargement, les favoris et le filtre avec combine
    val uiState: StateFlow<CryptoListUiState> = combine(
        _uiState,
        favoritesRepository.favorites,
        _showOnlyFavorites,
    ) { loadState, favorites, showOnlyFavorites ->
        if (loadState is CryptoListUiState.Success) {
            // Le filtrage se fait ici, dans le ViewModel
            val visibleCryptos = if (showOnlyFavorites) {
                loadState.cryptos.filter { it.symbol in favorites }
            } else {
                loadState.cryptos
            }
            CryptoListUiState.Success(
                cryptos = visibleCryptos,
                favorites = favorites,
                showOnlyFavorites = showOnlyFavorites,
            )
        } else {
            loadState // Loading ou Error : on ne change rien
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CryptoListUiState.Loading,
    )

    init {
        load()
    }

    fun refresh() = load()

    //  évolution 12, clic sur un cœur
    fun toggleFavorite(symbol: String) = favoritesRepository.toggle(symbol)

    //  évolution 12, clic sur le bouton filtre
    fun toggleShowOnlyFavorites() {
        _showOnlyFavorites.update { !it }
    }

    private fun load() {
        _uiState.value = CryptoListUiState.Loading
        viewModelScope.launch {
            try {
                _uiState.value = CryptoListUiState.Success(repository.getCryptos())
            } catch (e: Exception) {
                _uiState.value = CryptoListUiState.Error("Impossible de charger les cours.")
            }
        }
    }
}