package com.bvic.followmycrypto.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import com.bvic.followmycrypto.ui.components.CryptoLogo

@Composable
fun CryptoDetailScreen(
    symbol: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CryptoDetailViewModel = viewModel {
        CryptoDetailViewModel(symbol, AppContainer.cryptoRepository, AppContainer.favoritesRepository)
    },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // Évolution 12 : est-ce que cette crypto est en favori ?
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()

    CryptoDetailContent(
        symbol = symbol,
        uiState = uiState,
        isFavorite = isFavorite,
        onBack = onBack,
        onRetry = viewModel::retry,
        onToggleFavorite = viewModel::toggleFavorite,
        modifier = modifier,
    )
}

@Composable
fun CryptoDetailContent(
    symbol: String,
    uiState: CryptoDetailUiState,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(symbol, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
                // Évolution 12 : le cœur à droite de la barre du haut (plein = favori)
                actions = {
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = if (isFavorite) "Retirer des favoris" else "Ajouter aux favoris",
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding).fillMaxSize()
        when (uiState) {
            CryptoDetailUiState.Loading -> LoadingContent(modifier = contentModifier)
            is CryptoDetailUiState.Error -> ErrorContent(
                message = uiState.message,
                onRetry = onRetry,
                modifier = contentModifier,
            )
            is CryptoDetailUiState.Success -> CryptoDetail(crypto = uiState.crypto, modifier = contentModifier)
        }
    }
}

@Composable
fun CryptoDetail(crypto: Crypto, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CryptoLogo(crypto = crypto, size = 72.dp)
        Text(
            text = crypto.name,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = formatPrice(crypto.price),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "${formatPercent(crypto.changePercent)} sur 24h",
            style = MaterialTheme.typography.titleMedium,
            color = if (crypto.changePercent >= 0) PriceUp else PriceDown,
        )
        Spacer(modifier = Modifier.height(16.dp))
        PriceVariation(high24h = crypto.high24h, low24h = crypto.low24h)
    }
}

// Évolution 1 : le plus haut et le plus bas sur 24h
@Composable
fun PriceVariation(high24h: Double, low24h: Double, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Ligne 1 : le plus haut
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Plus haut 24h",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = formatPrice(high24h),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
            )
        }
        // Ligne 2 : le plus bas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Plus bas 24h",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = formatPrice(low24h),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CryptoDetailContentPreview() {
    FollowMyCryptoTheme {
        CryptoDetailContent(
            symbol = "BTC",
            uiState = CryptoDetailUiState.Success(
                Crypto(symbol = "BTC", name = "Bitcoin", price = 85613.50, changePercent = 0.88, high24h = 85666.01, low24h = 84120.00),
            ),
            isFavorite = true,
            onBack = {},
            onRetry = {},
            onToggleFavorite = {},
        )
    }
}