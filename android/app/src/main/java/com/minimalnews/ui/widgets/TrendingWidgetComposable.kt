package com.minimalnews.ui.widgets

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.minimalnews.data.models.GitHubTrending
import com.minimalnews.data.repository.Repository
import com.minimalnews.ui.components.*
import kotlinx.coroutines.launch

@Composable
fun TrendingWidgetComposable(repository: Repository) {
    var repos by remember { mutableStateOf<List<GitHubTrending>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    fun load() {
        scope.launch {
            loading = true; error = null
            try {
                repos = repository.fetchTrending()
            } catch (e: Exception) {
                error = e.message
            }
            loading = false
        }
    }

    LaunchedEffect(Unit) { load() }

    TerminalBox(
        title = "trending",
        status = "GitHub",
        onRefresh = { load() }
    ) {
        when {
            loading -> TerminalLoading("Fetching trending repos...")
            error != null -> TerminalError(error!!)
            else -> {
                repos.forEachIndexed { idx, repo ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                context.startActivity(
                                    Intent(Intent.ACTION_VIEW, Uri.parse(repo.url))
                                )
                            }
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = repo.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "★ ${formatNumber(repo.stars)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        if (repo.description.isNotBlank()) {
                            Text(
                                text = repo.description,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Text(
                            text = repo.language,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (idx < repos.lastIndex) TerminalDivider()
                }
            }
        }
    }
}
