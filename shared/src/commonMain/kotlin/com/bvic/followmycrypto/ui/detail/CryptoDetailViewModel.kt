package com.bvic.followmycrypto.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bvic.followmycrypto.data.CryptoRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CryptoDetailViewModel(
    private val symbol: String,
    private val repository: CryptoRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<CryptoDetailUiState>(CryptoDetailUiState.Loading)
    val uiState: StateFlow<CryptoDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun retry() = load()

    private fun load() {
        _uiState.value = CryptoDetailUiState.Loading
        viewModelScope.launch {
            try {
                _uiState.value = CryptoDetailUiState.Success(repository.getCrypto(symbol))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = CryptoDetailUiState.Error("Impossible de charger $symbol.")
            }
        }
    }
}
