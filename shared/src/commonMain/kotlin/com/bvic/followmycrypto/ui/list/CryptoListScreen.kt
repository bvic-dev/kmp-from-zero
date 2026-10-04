package com.bvic.followmycrypto.ui.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bvic.followmycrypto.model.Crypto
import com.bvic.followmycrypto.theme.FollowMyCryptoTheme
import com.bvic.followmycrypto.theme.PriceDown
import com.bvic.followmycrypto.theme.PriceUp
import com.bvic.followmycrypto.ui.format.formatPercent
import com.bvic.followmycrypto.ui.format.formatPrice

private val mockCryptos = listOf(
    Crypto(symbol = "BTC", name = "Bitcoin", price = 85613.50, changePercent = 0.88),
    Crypto(symbol = "ETH", name = "Ethereum", price = 3214.07, changePercent = -1.42),
    Crypto(symbol = "BNB", name = "BNB", price = 642.30, changePercent = 0.15),
    Crypto(symbol = "SOL", name = "Solana", price = 148.92, changePercent = 4.31),
    Crypto(symbol = "XRP", name = "XRP", price = 0.5873, changePercent = -0.64),
    Crypto(symbol = "ADA", name = "Cardano", price = 0.4521, changePercent = 2.07),
    Crypto(symbol = "DOGE", name = "Dogecoin", price = 0.1284, changePercent = -3.95),
    Crypto(symbol = "SHIB", name = "Shiba Inu", price = 0.00001834, changePercent = 1.12),
)

@Composable
fun CryptoListScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("FollowMyCrypto", fontWeight = FontWeight.Bold) },
            )
        },
    ) { innerPadding ->
        CryptoList(cryptos = mockCryptos, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun CryptoList(cryptos: List<Crypto>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(items = cryptos, key = { it.symbol }) { crypto ->
            CryptoRow(crypto = crypto)
            HorizontalDivider()
        }
    }
}

@Composable
fun CryptoRow(crypto: Crypto, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
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
    }
}

@Preview(showBackground = true)
@Composable
fun CryptoRowPreview() {
    FollowMyCryptoTheme {
        CryptoRow(crypto = mockCryptos.first())
    }
}

@Preview(showBackground = true)
@Composable
fun CryptoListScreenPreview() {
    FollowMyCryptoTheme {
        CryptoListScreen()
    }
}
