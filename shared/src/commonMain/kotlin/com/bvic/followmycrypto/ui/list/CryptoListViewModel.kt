package com.bvic.followmycrypto.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bvic.followmycrypto.data.CryptoRepository
import com.bvic.followmycrypto.model.*
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CryptoListViewModel(
    private val repository: CryptoRepository,
) : ViewModel() {

    private val _selectedSort = MutableStateFlow(CryptoSort.NAME_ASC)
    val selectedSort: StateFlow<CryptoSort> = _selectedSort.asStateFlow()

    private val _selectedDevise = MutableStateFlow(Devise.USDT)
    val selectedDevise: StateFlow<Devise> = _selectedDevise.asStateFlow()

    private var rawCryptos: List<Crypto> = emptyList()

    // Version modifiable, privée : seul le ViewModel peut changer l'état
    private val _uiState = MutableStateFlow<CryptoListUiState>(CryptoListUiState.Loading)

    // Version en lecture seule, exposée à l'interface
    val uiState: StateFlow<CryptoListUiState> = _uiState.asStateFlow()

    init {
        load()
        startAutoRefresh()
    }

    fun refresh() = load()

    fun startAutoRefresh() {
        viewModelScope.launch {
            while (isActive) {
                delay(10.seconds)
                try {
                    val fetched = repository.getCryptos(_selectedDevise.value)
                    rawCryptos = fetched
                    val currentSort = _selectedSort.value
                    _uiState.value = CryptoListUiState.Success(
                        cryptos = currentSort.sort(fetched),
                        selectedSort = currentSort,
                        selectedDevise = _selectedDevise.value,
                    )
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {}
            }
        }
    }

    fun addCrypto(symbol: String, name: String) {
        if (symbol.isNotBlank() && name.isNotBlank()) {
            repository.addCrypto(symbol, name)
            load()
        }
    }

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

    fun onDeviseSelected(devise: Devise) {
        if (_selectedDevise.value != devise) {
            _selectedDevise.value = devise
            load()
        }
    }

    private fun load() {
        _uiState.value = CryptoListUiState.Loading
        viewModelScope.launch {
            try {
                val fetched = repository.getCryptos(_selectedDevise.value)
                rawCryptos = fetched
                val currentSort = _selectedSort.value
                _uiState.value = CryptoListUiState.Success(
                    cryptos = currentSort.sort(fetched),
                    selectedSort = currentSort,
                    selectedDevise = _selectedDevise.value,
                )
            } catch (e: Exception) {
                _uiState.value = CryptoListUiState.Error("Impossible de charger les cours.")
            }
        }
    }
}
