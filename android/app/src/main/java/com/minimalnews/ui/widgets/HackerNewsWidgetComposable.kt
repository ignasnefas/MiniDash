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
import com.minimalnews.data.models.HackerNewsItem
import com.minimalnews.data.repository.Repository
import com.minimalnews.ui.components.*
import kotlinx.coroutines.launch

@Composable
fun HackerNewsWidgetComposable(repository: Repository) {
    var stories by remember { mutableStateOf<List<HackerNewsItem>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var storyType by remember { mutableStateOf("top") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    fun load() {
        scope.launch {
            loading = true; error = null
            try {
                stories = repository.fetchHackerNews(storyType, 10)
            } catch (e: Exception) {
                error = e.message
            }
            loading = false
        }
    }

    LaunchedEffect(storyType) { load() }

    TerminalBox(
        title = "hackernews",
        status = storyType,
        onRefresh = { load() }
    ) {
        TerminalFilterRow(
            options = listOf("top", "new", "best", "ask", "show"),
            selected = storyType,
            onSelect = { storyType = it }
        )
        Spacer(Modifier.height(8.dp))

        when {
            loading -> TerminalLoading("Fetching stories...")
            error != null -> TerminalError(error!!)
            else -> {
                // Table header
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("#", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, modifier = Modifier.width(24.dp))
                    Text("Score", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, modifier = Modifier.width(48.dp))
                    Text("Title", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))
                }
                TerminalDivider()

                stories.forEachIndexed { idx, story ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val url = story.url ?: "https://news.ycombinator.com/item?id=${story.id}"
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                            }
                            .padding(vertical = 4.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.Top
                    ) {
                        Text(
                            text = "${idx + 1}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.width(24.dp)
                        )
                        Text(
                            text = formatNumber(story.score),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.width(48.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = story.title,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 2
                            )
                            val domain = story.url?.let {
                                try { Uri.parse(it).host?.removePrefix("www.") } catch (_: Exception) { null }
                            }
                            Text(
                                text = buildString {
                                    append("by ${story.by}")
                                    if (domain != null) append(" | $domain")
                                    append(" | ${story.descendants} comments")
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    if (idx < stories.lastIndex) TerminalDivider()
                }
            }
        }
    }
}
