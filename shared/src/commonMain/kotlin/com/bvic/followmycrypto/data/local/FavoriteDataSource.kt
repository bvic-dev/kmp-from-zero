package com.bvic.followmycrypto.data.local

import com.russhwolf.settings.Settings

class FavoriteDataSource(
    private val settings: Settings = Settings(),
) {
    companion object {
        private const val FAVORITES_KEY = "favorite_crypto_symbols"
    }

    fun getFavorites(): Set<String> {
        return settings.getString(FAVORITES_KEY, "")
            .split(",")
            .filter { it.isNotBlank() }
            .toSet()
    }

    fun saveFavorites(favorites: Set<String>) {
        settings.putString(
            FAVORITES_KEY,
            favorites.sorted().joinToString(","),
        )
    }
}
