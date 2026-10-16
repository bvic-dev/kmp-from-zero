
package com.bvic.followmycrypto.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bvic.followmycrypto.data.CryptoRepository
import com.bvic.followmycrypto.data.FavoriteRepository
import com.bvic.followmycrypto.model.Currency
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CryptoListViewModel(
    private val repository: CryptoRepository,
    private val favoriteRepository: FavoriteRepository,
) : ViewModel() {

    private val _sourceUiState =
        MutableStateFlow<CryptoListUiState>(CryptoListUiState.Loading)

    private val _favoritesOnly = MutableStateFlow(false)

    private val _currency = MutableStateFlow(Currency.USD)

    val currency: StateFlow<Currency> = _currency.asStateFlow()

    private var loadJob: Job? = null

    val favoritesOnly: StateFlow<Boolean> =
        _favoritesOnly.asStateFlow()

    val favorites: StateFlow<Set<String>> =
        favoriteRepository.favorites

    val uiState: StateFlow<CryptoListUiState> =
        combine(
            _sourceUiState,
            favoriteRepository.favorites,
            _favoritesOnly,
        ) { state, favoriteSymbols, onlyFavorites ->

            if (state is CryptoListUiState.Success && onlyFavorites) {
                state.copy(
                    cryptos = state.cryptos.filter {
                        it.symbol in favoriteSymbols
                    }
                )
            } else {
                state
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

    fun changeCurrency(newCurrency: Currency) {
        if (_currency.value == newCurrency) return

        _currency.value = newCurrency
        load()
    }

    fun toggleFavorite(symbol: String) {
        favoriteRepository.toggleFavorite(symbol)
    }

    fun toggleFavoritesFilter() {
        _favoritesOnly.value = !_favoritesOnly.value
    }

    private fun load() {
        loadJob?.cancel()

        val selectedCurrency = _currency.value

        _sourceUiState.value = CryptoListUiState.Loading

        loadJob = viewModelScope.launch {
            try {
                val cryptos = repository.getCryptos(selectedCurrency)

                _sourceUiState.value =
                    CryptoListUiState.Success(cryptos)

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _sourceUiState.value =
                    CryptoListUiState.Error(
                        "Impossible de charger les cours."
                    )
            }
        }
    }
}
