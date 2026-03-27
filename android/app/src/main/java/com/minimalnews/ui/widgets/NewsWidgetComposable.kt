package com.minimalnews.ui.widgets

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.minimalnews.data.models.NewsItem
import com.minimalnews.data.repository.Repository
import com.minimalnews.ui.components.*
import kotlinx.coroutines.launch

@Composable
fun NewsWidgetComposable(repository: Repository) {
    var news by remember { mutableStateOf<List<NewsItem>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var category by remember { mutableStateOf("all") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    fun loadNews() {
        scope.launch {
            loading = true; error = null
            try {
                news = repository.fetchNews(category)
            } catch (e: Exception) {
                error = e.message
            }
            loading = false
        }
    }

    LaunchedEffect(category) { loadNews() }

    TerminalBox(
        title = "news",
        status = "${news.size} articles",
        onRefresh = { loadNews() }
    ) {
        TerminalFilterRow(
            options = listOf("all", "BBC", "NPR", "Guardian", "CNN"),
            selected = category,
            onSelect = { category = it }
        )
        Spacer(Modifier.height(8.dp))

        when {
            loading -> TerminalLoading("Fetching news...")
            error != null -> TerminalError(error!!)
            news.isEmpty() -> TerminalError("No articles found")
            else -> {
                val filtered = if (category == "all") news
                else news.filter { it.source.equals(category, ignoreCase = true) }

                filtered.forEachIndexed { idx, item ->
                    TerminalListItem(
                        index = idx + 1,
                        title = item.title,
                        meta = "${item.source} · ${item.publishedAt.take(16)}",
                        onClick = {
                            if (item.url.isNotBlank()) {
                                context.startActivity(
                                    Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                                )
                            }
                        }
                    )
                    if (idx < filtered.lastIndex) TerminalDivider()
                }
            }
        }
    }
}
