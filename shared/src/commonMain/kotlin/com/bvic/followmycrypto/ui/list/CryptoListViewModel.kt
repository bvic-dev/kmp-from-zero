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

    private val _selectedSort = MutableStateFlow(CryptoSort.NAME_ASC)

    private var rawCryptos: List<Crypto> = emptyList()

    // Version modifiable, privée : seul le ViewModel peut changer l'état
    private val _uiState = MutableStateFlow<CryptoListUiState>(CryptoListUiState.Loading)

    // Version en lecture seule, exposée à l'interface
    val uiState: StateFlow<CryptoListUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun refresh() = load()

    fun onSortSelected(sort: CryptoSort) {
        _selectedSort.value = sort
        val current = _uiState.value
        if (current is CryptoListUiState.Success) {
            _uiState.value = current.copy(
                cryptos = sort.sort(rawCryptos),
                selectedSort = sort,
            )
        }
    }

    private fun load() {
        _uiState.value = CryptoListUiState.Loading
        viewModelScope.launch {
            try {
                rawCryptos = repository.getCryptos()
                val currentSort = _selectedSort.value
                _uiState.value = CryptoListUiState.Success(
                    cryptos = currentSort.sort(rawCryptos),
                    selectedSort = currentSort,
                )
            } catch (e: Exception) {
                _uiState.value = CryptoListUiState.Error("Impossible de charger les cours.")
            }
        }
    }
}
