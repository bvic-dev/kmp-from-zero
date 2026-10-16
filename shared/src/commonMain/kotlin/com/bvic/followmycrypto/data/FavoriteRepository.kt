
package com.bvic.followmycrypto.data

import com.bvic.followmycrypto.data.local.FavoriteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FavoriteRepository(
    private val localDataSource: FavoriteDataSource =
        FavoriteDataSource(),
) {
    private val _favorites =
        MutableStateFlow(localDataSource.getFavorites())

    val favorites: StateFlow<Set<String>> =
        _favorites.asStateFlow()

    fun toggleFavorite(symbol: String) {
        _favorites.update { current ->
            val updated = if (symbol in current) {
                current - symbol
            } else {
                current + symbol
            }

            localDataSource.saveFavorites(updated)
            updated
        }
    }
}
