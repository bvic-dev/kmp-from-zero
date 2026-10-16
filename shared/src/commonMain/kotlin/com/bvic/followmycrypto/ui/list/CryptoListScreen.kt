package com.bvic.followmycrypto.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.SubcomposeAsyncImage
import com.bvic.followmycrypto.data.AppContainer
import com.bvic.followmycrypto.model.*
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
        onDeviseSelected = viewModel::onDeviseSelected,
        onAddCrypto = viewModel::addCrypto,
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
    onDeviseSelected: (Devise) -> Unit = {},
    onAddCrypto: (symbol: String, name: String) -> Unit = { _, _ -> },
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
                selectedDevise = uiState.selectedDevise,
                onSortSelected = onSortSelected,
                onDeviseSelected = onDeviseSelected,
                onAddCrypto = onAddCrypto,
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
    selectedDevise: Devise,
    onSortSelected: (CryptoSort) -> Unit,
    onDeviseSelected: (Devise) -> Unit,
    onAddCrypto: (symbol: String, name: String) -> Unit,
    onCryptoClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showAddDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        DeviseChips(
            selectedDevise = selectedDevise,
            onDeviseSelected = onDeviseSelected,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
        SortChips(
            selectedSort = selectedSort,
            onSortSelected = onSortSelected,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )

        HorizontalDivider()
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(items = cryptos, key = { it.symbol }) { crypto ->
                CryptoRow(
                    crypto = crypto,
                    devise = selectedDevise,
                    onClick = { onCryptoClick(crypto.symbol) },
                )
                HorizontalDivider()
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    OutlinedButton(
                        onClick = { showAddDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Text("Ajouter une crypto")
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddCryptoDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, symbol ->
                onAddCrypto(symbol, name)
                showAddDialog = false
            },
        )
    }
}

@Composable
fun AddCryptoDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, symbol: String) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var symbol by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ajouter une nouvelle crypto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom (ex: Bitcoin)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = symbol,
                    onValueChange = { symbol = it },
                    label = { Text("Symbole (ex: BTC)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(name.trim(), symbol.trim()) },
            ) {
                Text("Ajouter")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        },
    )
}

@Composable
fun DeviseChips(
    selectedDevise: Devise,
    onDeviseSelected: (Devise) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        item {
            FilterChip(
                selected = selectedDevise == Devise.USDT,
                onClick = { onDeviseSelected(Devise.USDT) },
                label = { Text("USDT ($)") },
            )
        }

        item {
            FilterChip(
                selected = selectedDevise == Devise.EUR,
                onClick = { onDeviseSelected(Devise.EUR) },
                label = { Text("EUR (€)") },
            )
        }
        item {
            FilterChip(
                selected = selectedDevise == Devise.JPY,
                onClick = { onDeviseSelected(Devise.JPY) },
                label = { Text("JPY (¥)") },
            )
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
fun CryptoIcon(
    symbol: String,
    modifier: Modifier = Modifier,
) {
    val logoUrl = "https://cdn.jsdelivr.net/gh/spothq/cryptocurrency-icons@master/128/color/${symbol.lowercase()}.png"
    val placeholder = @Composable {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = symbol.take(3).uppercase(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }

    SubcomposeAsyncImage(
        model = logoUrl,
        contentDescription = null,
        loading = { placeholder() },
        error = { placeholder() },
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape),
    )
}

@Composable
fun CryptoRow(
    crypto: Crypto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    devise: Devise = Devise.USDT,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CryptoIcon(
            symbol = crypto.symbol,
            modifier = Modifier.padding(end = 12.dp),
        )
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
                text = formatPrice(crypto.price, devise),
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
