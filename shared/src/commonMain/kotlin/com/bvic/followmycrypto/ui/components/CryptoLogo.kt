package com.bvic.followmycrypto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.bvic.followmycrypto.model.Crypto

// Le logo d'une crypto, chargé depuis Internet.
// Tant qu'il charge, ou s'il n'existe pas, on affiche l'initiale à la place.
@Composable
fun CryptoLogo(
    crypto: Crypto,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
) {
    SubcomposeAsyncImage(
        model = crypto.logoUrl,
        contentDescription = null, // le nom est déjà écrit à côté : le logo est décoratif
        modifier = modifier.size(size).clip(CircleShape),
        loading = { LogoPlaceholder(crypto.symbol) },
        error = { LogoPlaceholder(crypto.symbol) },
    )
}

@Composable
private fun LogoPlaceholder(symbol: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = symbol.take(1),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Bold,
        )
    }
}