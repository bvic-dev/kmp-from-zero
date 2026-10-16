package com.bvic.followmycrypto.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bvic.followmycrypto.data.CryptoRepository
import com.bvic.followmycrypto.model.Crypto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CryptoListViewModel(
    private val repository: CryptoRepository,
) : ViewModel() {

    // Version modifiable, privée : seul le ViewModel peut changer l'état
    private val _uiState = MutableStateFlow<CryptoListUiState>(CryptoListUiState.Loading)
    // Garder la liste complète permet de retrouver toutes les cryptos après une recherche.
    private var allCryptos: List<Crypto> = emptyList()
    // Ces choix restent mémorisés pendant le rafraîchissement.
    private var selectedSort = CryptoSort.NAME
    private var searchQuery = ""

    // Version en lecture seule, exposée à l'interface
    val uiState: StateFlow<CryptoListUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun refresh() = load()

    fun onSortChange(sort: CryptoSort) {
        selectedSort = sort
        updateDisplayedCryptos()
    }

    fun onQueryChange(query: String) {
        searchQuery = query
        updateDisplayedCryptos()
    }

    private fun updateDisplayedCryptos() {
        // Ignorer les espaces autour de la recherche sans modifier le texte du champ.
        val query = searchQuery.trim()

        // Repartir de toutes les cryptos, avec une correspondance sur le nom ou le symbole.
        val filtered = allCryptos.filter { crypto ->
            crypto.name.contains(query, ignoreCase = true) ||
                    crypto.symbol.contains(query, ignoreCase = true)
        }

        // Trier les résultats par nom croissant, prix décroissant ou variation décroissante.
        val sorted = when (selectedSort) {
            CryptoSort.NAME -> filtered.sortedBy { it.name.lowercase() }
            CryptoSort.PRICE -> filtered.sortedByDescending { it.price }
            CryptoSort.CHANGE -> filtered.sortedByDescending { it.changePercent }
        }

        _uiState.value = CryptoListUiState.Success(
            cryptos = sorted,
            sort = selectedSort,
            query = searchQuery,
        )
    }

    private fun load() {
        _uiState.value = CryptoListUiState.Loading
        viewModelScope.launch {
            try {
                allCryptos = repository.getCryptos()
                // Réappliquer la recherche et le tri aux nouvelles données.
                updateDisplayedCryptos()
            } catch (e: Exception) {
                _uiState.value = CryptoListUiState.Error("Impossible de charger les cours.")
            }
        }
    }
}
