package com.bvic.followmycrypto.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@Composable
fun CryptoListScreen(
    onCryptoClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CryptoListViewModel = viewModel { CryptoListViewModel(AppContainer.cryptoRepository) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CryptoListContent(
        uiState = uiState,
        onRefresh = viewModel::refresh,
        onSortSelected = viewModel::onSortSelected,
        onCryptoClick = onCryptoClick,
        modifier = modifier,
    )
}

// Version sans état : elle ne connaît pas le ViewModel, on peut donc l'afficher dans un aperçu
@Composable
fun CryptoListContent(
    uiState: CryptoListUiState,
    onRefresh: () -> Unit,
    onCryptoClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSortSelected: (CryptoSort) -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("FollowMyCrypto", fontWeight = FontWeight.Bold) },
                actions = {
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
            is CryptoListUiState.Success -> CryptoList(
                cryptos = uiState.cryptos,
                selectedSort = uiState.selectedSort,
                onSortSelected = onSortSelected,
                onCryptoClick = onCryptoClick,
                modifier = contentModifier,
            )
        }
    }
}

@Composable
fun CryptoList(
    cryptos: List<Crypto>,
    selectedSort: CryptoSort,
    onSortSelected: (CryptoSort) -> Unit,
    onCryptoClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        SortChips(
            selectedSort = selectedSort,
            onSortSelected = onSortSelected,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
        HorizontalDivider()
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(items = cryptos, key = { it.symbol }) { crypto ->
                CryptoRow(crypto = crypto, onClick = { onCryptoClick(crypto.symbol) })
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun SortChips(
    selectedSort: CryptoSort,
    onSortSelected: (CryptoSort) -> Unit,
    modifier: Modifier = Modifier,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val isNameSelected = selectedSort == CryptoSort.NAME_ASC || selectedSort == CryptoSort.NAME_DESC
    val isPriceSelected = selectedSort == CryptoSort.PRICE_DESC || selectedSort == CryptoSort.PRICE_ASC
    val isChangeSelected = selectedSort == CryptoSort.CHANGE_DESC || selectedSort == CryptoSort.CHANGE_ASC

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        item {
            Box {
                FilterChip(
                    selected = false,
                    onClick = { menuExpanded = true },
                    label = { Text("Menu") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize),
                        )
                    },
                )

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                ) {
                    CryptoSort.entries.forEach { sort ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = sort.label,
                                    fontWeight = if (sort == selectedSort) FontWeight.Bold else FontWeight.Normal,
                                )
                            },
                            onClick = {
                                onSortSelected(sort)
                                menuExpanded = false
                            },
                        )
                    }
                }
            }
        }

        item {
            FilterChip(
                selected = isNameSelected,
                onClick = {
                    if (selectedSort == CryptoSort.NAME_ASC) {
                        onSortSelected(CryptoSort.NAME_DESC)
                    } else {
                        onSortSelected(CryptoSort.NAME_ASC)
                    }
                },
                label = { Text("Nom") },
                trailingIcon = if (isNameSelected) {
                    {
                        Icon(
                            imageVector = if (selectedSort == CryptoSort.NAME_ASC) {
                                Icons.Filled.KeyboardArrowUp
                            } else {
                                Icons.Filled.KeyboardArrowDown
                            },
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize),
                        )
                    }
                } else null,
            )
        }

        item {
            FilterChip(
                selected = isPriceSelected,
                onClick = {
                    if (selectedSort == CryptoSort.PRICE_DESC) {
                        onSortSelected(CryptoSort.PRICE_ASC)
                    } else {
                        onSortSelected(CryptoSort.PRICE_DESC)
                    }
                },
                label = { Text("Prix") },
                trailingIcon = if (isPriceSelected) {
                    {
                        Icon(
                            imageVector = if (selectedSort == CryptoSort.PRICE_DESC) {
                                Icons.Filled.KeyboardArrowDown
                            } else {
                                Icons.Filled.KeyboardArrowUp
                            },
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize),
                        )
                    }
                } else null,
            )
        }

        item {
            FilterChip(
                selected = isChangeSelected,
                onClick = {
                    if (selectedSort == CryptoSort.CHANGE_DESC) {
                        onSortSelected(CryptoSort.CHANGE_ASC)
                    } else {
                        onSortSelected(CryptoSort.CHANGE_DESC)
                    }
                },
                label = { Text("Variation") },
                trailingIcon = if (isChangeSelected) {
                    {
                        Icon(
                            imageVector = if (selectedSort == CryptoSort.CHANGE_DESC) {
                                Icons.Filled.KeyboardArrowDown
                            } else {
                                Icons.Filled.KeyboardArrowUp
                            },
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize),
                        )
                    }
                } else null,
            )
        }
    }
}

@Composable
fun CryptoRow(crypto: Crypto, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
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

private val previewCryptos = listOf(
    Crypto(symbol = "BTC", name = "Bitcoin", price = 85613.50, changePercent = 0.88, 85613.50),
    Crypto(symbol = "ETH", name = "Ethereum", price = 3214.07, changePercent = -1.42, 3214.07),
)

@Preview(showBackground = true)
/*@Composable
fun CryptoRowPreview() {
    FollowMyCryptoTheme {
        CryptoRow(crypto = previewCryptos.first(), onClick = {})
    }
}*/

@Preview(showBackground = true)
@Composable
fun CryptoListContentPreview() {
    FollowMyCryptoTheme {
        CryptoListContent(uiState = CryptoListUiState.Success(previewCryptos), onRefresh = {}, onCryptoClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun CryptoListErrorPreview() {
    FollowMyCryptoTheme {
        CryptoListContent(uiState = CryptoListUiState.Error("Impossible de charger les cours."), onRefresh = {}, onCryptoClick = {})
    }
}
