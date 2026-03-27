package com.minimalnews.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.minimalnews.data.models.CryptoPrice
import com.minimalnews.data.repository.Repository
import com.minimalnews.ui.components.*
import kotlinx.coroutines.launch

@Composable
fun CryptoWidgetComposable(repository: Repository) {
    var prices by remember { mutableStateOf<List<CryptoPrice>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    fun load() {
        scope.launch {
            loading = true; error = null
            try {
                prices = repository.fetchCrypto()
            } catch (e: Exception) {
                error = e.message
            }
            loading = false
        }
    }

    LaunchedEffect(Unit) { load() }

    TerminalBox(
        title = "crypto",
        status = "${prices.size} tokens",
        onRefresh = { load() }
    ) {
        when {
            loading -> TerminalLoading("Fetching prices...")
            error != null -> TerminalError(error!!)
            else -> {
                // Table header
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Token", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))
                    Text("Price", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, modifier = Modifier.width(80.dp))
                    Text("24h", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, modifier = Modifier.width(64.dp))
                }
                TerminalDivider()

                prices.forEach { crypto ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = crypto.symbol,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = crypto.name,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "$${formatPrice(crypto.price)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.width(80.dp)
                        )
                        Text(
                            text = "${if (crypto.change24h >= 0) "+" else ""}${"%.1f".format(crypto.change24h)}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (crypto.change24h >= 0) MaterialTheme.colorScheme.secondary
                            else MaterialTheme.colorScheme.error,
                            modifier = Modifier.width(64.dp)
                        )
                    }
                    TerminalDivider()
                }
            }
        }
    }
}

private fun formatPrice(price: Double): String = when {
    price >= 1000 -> "%,.0f".format(price)
    price >= 1 -> "%.2f".format(price)
    price >= 0.01 -> "%.4f".format(price)
    else -> "%.6f".format(price)
}
