package com.bvic.followmycrypto.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.bvic.followmycrypto.model.Crypto
import com.bvic.followmycrypto.theme.FollowMyCryptoTheme

@Composable
fun CryptoLogo(crypto: Crypto, modifier: Modifier = Modifier) {
    val painter = rememberAsyncImagePainter(crypto.logoUrl)
    val state by painter.state.collectAsState()

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        // L’image reste composée pour permettre au chargement de démarrer.
        Image(
            painter = painter,
            contentDescription = "Logo de ${crypto.name}",
            modifier = Modifier.matchParentSize(),
        )
        if (state !is AsyncImagePainter.State.Success) {
            Text(
                text = crypto.symbol.take(3).uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CryptoLogoPreview() {
    FollowMyCryptoTheme {
        CryptoLogo(
            crypto = Crypto(symbol = "BTC", name = "Bitcoin", price = 85000.0, changePercent = 1.0),
            modifier = Modifier.size(48.dp),
        )
    }
}
