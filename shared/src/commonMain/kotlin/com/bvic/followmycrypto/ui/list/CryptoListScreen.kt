package com.bvic.followmycrypto.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box // ← AJOUT
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite // ← AJOUT
import androidx.compose.material.icons.filled.FavoriteBorder // ← AJOUT
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bvic.followmycrypto.data.AppContainer
import com.bvic.followmycrypto.model.Crypto
import com.bvic.followmycrypto.theme.FollowMyCryptoTheme
import com.bvic.followmycrypto.theme.PriceDown
import com.bvic.followmycrypto.theme.PriceUp
import com.bvic.followmycrypto.ui.components.ErrorContent
import com.bvic.followmycrypto.ui.components.LoadingContent
import com.bvic.followmycrypto.ui.format.formatPercent
import com.bvic.followmycrypto.ui.format.formatPrice
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import com.bvic.followmycrypto.ui.components.CryptoLogo

@Composable
fun CryptoListScreen(
    onCryptoClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CryptoListViewModel = viewModel { CryptoListViewModel(AppContainer.cryptoRepository , AppContainer.favoritesRepository) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CryptoListContent(
        uiState = uiState,
        onRefresh = viewModel::refresh,
        onCryptoClick = onCryptoClick,
        onToggleFavorite = viewModel::toggleFavorite, //: clic sur un cœur → ViewModel
        onToggleShowOnlyFavorites = viewModel::toggleShowOnlyFavorites, // : clic sur le filtre → ViewModel
        modifier = modifier,
    )
}

// Version sans état : elle ne connaît pas le ViewModel, on peut donc l'afficher dans un aperçu
@Composable
fun CryptoListContent(
    uiState: CryptoListUiState,
    onRefresh: () -> Unit,
    onCryptoClick: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onToggleShowOnlyFavorites: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("FollowMyCrypto", fontWeight = FontWeight.Bold) },
                actions = {
                    // ← AJOUT : le bouton filtre « favoris seulement » (cœur plein = filtre activé)
                    val showOnlyFavorites = uiState is CryptoListUiState.Success && uiState.showOnlyFavorites
                    IconButton(onClick = onToggleShowOnlyFavorites) {
                        Icon(
                            imageVector = if (showOnlyFavorites) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Afficher seulement les favoris",
                            tint = if (showOnlyFavorites) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Rafraîchir")
                    }
                },
            )
        },
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding).fillMaxSize()
        when (uiState) {
            CryptoListUiState.Loading -> LoadingContent(modifier = contentModifier)
            is CryptoListUiState.Error -> ErrorContent(
                message = uiState.message,
                onRetry = onRefresh,
                modifier = contentModifier,
            )
            is CryptoListUiState.Success -> {
                // ← AJOUT : filtre activé mais aucun favori → on le dit au lieu d'afficher une liste vide
                if (uiState.showOnlyFavorites && uiState.cryptos.isEmpty()) {
                    Box(modifier = contentModifier, contentAlignment = Alignment.Center) {
                        Text(
                            text = "Aucun favori pour l'instant",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                } else {
                    CryptoList(
                        cryptos = uiState.cryptos,
                        favorites = uiState.favorites,
                        onCryptoClick = onCryptoClick,
                        onToggleFavorite = onToggleFavorite,
                        modifier = contentModifier,
                    )
                }
            }
        }
    }
}

@Composable
fun CryptoList(
    cryptos: List<Crypto>,
    favorites: Set<String>, //  les symboles en favori
    onCryptoClick: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(items = cryptos, key = { it.symbol }) { crypto ->
            CryptoRow(
                crypto = crypto,
                isFavorite = crypto.symbol in favorites, // est-ce que CETTE crypto est en favori ?
                onClick = { onCryptoClick(crypto.symbol) },
                onToggleFavorite = { onToggleFavorite(crypto.symbol) },
            ) //  CryptoRow reçoit 2 infos de plus
            HorizontalDivider()
        }
    }
}

@Composable
fun CryptoRow(
    crypto: Crypto,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CryptoLogo(crypto = crypto)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = crypto.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = crypto.symbol,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formatPrice(crypto.price),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = formatPercent(crypto.changePercent),
                style = MaterialTheme.typography.bodySmall,
                color = if (crypto.changePercent >= 0) PriceUp else PriceDown,
            )
        }
        //  le cœur, à droite du prix (plein si favori, vide sinon)
        IconButton(onClick = onToggleFavorite) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = if (isFavorite) "Retirer des favoris" else "Ajouter aux favoris",
                tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private val previewCryptos = listOf(
    Crypto(symbol = "BTC", name = "Bitcoin", price = 85613.50, changePercent = 0.88, high24h = 85666.01, low24h = 84120.00),
    Crypto(symbol = "ETH", name = "Ethereum", price = 3214.07, changePercent = -1.42 , high24h = 3290.50, low24h = 3180.20),
)

@Preview(showBackground = true)
@Composable
fun CryptoRowPreview() {
    FollowMyCryptoTheme {
        CryptoRow(crypto = previewCryptos.first(), isFavorite = true, onClick = {}, onToggleFavorite = {}) // ← MODIFIÉ
    }
}

@Preview(showBackground = true)
@Composable
fun CryptoListContentPreview() {
    FollowMyCryptoTheme {
        CryptoListContent(
            uiState = CryptoListUiState.Success(previewCryptos, favorites = setOf("BTC")), //  BTC en favori
            onRefresh = {},
            onCryptoClick = {},
            onToggleFavorite = {},
            onToggleShowOnlyFavorites = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CryptoListErrorPreview() {
    FollowMyCryptoTheme {
        CryptoListContent(
            uiState = CryptoListUiState.Error("Impossible de charger les cours."),
            onRefresh = {},
            onCryptoClick = {},
            onToggleFavorite = {},
            onToggleShowOnlyFavorites = {},
        )
    }
}