package com.minimalnews.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.minimalnews.data.models.Quote
import com.minimalnews.data.repository.Repository
import com.minimalnews.ui.components.TerminalBox
import com.minimalnews.ui.components.TerminalLoading
import com.minimalnews.ui.components.TerminalError
import kotlinx.coroutines.launch

@Composable
fun QuoteWidgetComposable(repository: Repository) {
    var quote by remember { mutableStateOf<Quote?>(null) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val clipboard = LocalClipboardManager.current

    fun loadQuote() {
        scope.launch {
            loading = true; error = null
            try {
                quote = repository.fetchQuote()
            } catch (e: Exception) {
                error = e.message
            }
            loading = false
        }
    }

    LaunchedEffect(Unit) { loadQuote() }

    TerminalBox(title = "quote", onRefresh = { loadQuote() }) {
        when {
            loading -> TerminalLoading("Fetching quote...")
            error != null -> TerminalError(error!!)
            quote != null -> {
                Text(
                    text = "\"${quote!!.text}\"",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontStyle = FontStyle.Italic
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "— ${quote!!.author ?: "Unknown"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "[copy]",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            clipboard.setText(
                                AnnotatedString("\"${quote!!.text}\" — ${quote!!.author ?: "Unknown"}")
                            )
                        }
                    )
                }
            }
        }
    }
}
