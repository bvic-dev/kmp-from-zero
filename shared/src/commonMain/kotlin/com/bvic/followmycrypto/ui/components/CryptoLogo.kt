package com.bvic.followmycrypto.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import com.bvic.followmycrypto.model.Crypto

@Composable
fun CryptoLogo(
    crypto: Crypto,
    modifier: Modifier = Modifier
) {
    var isLoaded by remember(crypto.logoUrl) {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (!isLoaded) {
            Text(crypto.symbol.take(2).uppercase())
        }

        AsyncImage(
            model = crypto.logoUrl,
            contentDescription = "Logo de ${crypto.name}",
            modifier = Modifier.fillMaxSize(),
            onSuccess = { isLoaded = true },
            onError = { isLoaded = false }
        )
    }
}