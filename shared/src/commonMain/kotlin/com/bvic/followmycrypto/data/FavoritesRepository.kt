package com.bvic.followmycrypto.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Le contrat : « je sais gérer la liste des favoris »
interface FavoritesRepository {
    // Les symboles favoris, ex. {"BTC", "SOL"}
    val favorites: StateFlow<Set<String>>

    // Ajoute le symbole s'il n'y est pas, l'enlève s'il y est
    fun toggle(symbol: String)
}

// Version : les favoris sont gardés en mémoire (perdus à la fermeture de l'app)
class InMemoryFavoritesRepository : FavoritesRepository {

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    override val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    override fun toggle(symbol: String) {
        _favorites.update { current ->
            if (symbol in current) current - symbol else current + symbol
        }
    }
}