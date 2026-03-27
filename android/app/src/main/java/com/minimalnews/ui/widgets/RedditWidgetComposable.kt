package com.minimalnews.ui.widgets

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.minimalnews.data.models.RedditPost
import com.minimalnews.data.repository.Repository
import com.minimalnews.ui.components.*
import kotlinx.coroutines.launch

@Composable
fun RedditWidgetComposable(repository: Repository) {
    var posts by remember { mutableStateOf<List<RedditPost>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var subreddit by remember { mutableStateOf("all") }
    var sort by remember { mutableStateOf("hot") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    fun load() {
        scope.launch {
            loading = true; error = null
            try {
                posts = repository.fetchReddit(subreddit, sort)
            } catch (e: Exception) {
                error = e.message
            }
            loading = false
        }
    }

    LaunchedEffect(subreddit, sort) { load() }

    TerminalBox(
        title = "reddit",
        status = "r/$subreddit",
        onRefresh = { load() }
    ) {
        TerminalFilterRow(
            options = listOf("all", "programming", "technology", "science", "worldnews"),
            selected = subreddit,
            onSelect = { subreddit = it }
        )
        Spacer(Modifier.height(4.dp))
        TerminalFilterRow(
            options = listOf("hot", "new", "top", "rising"),
            selected = sort,
            onSelect = { sort = it }
        )
        Spacer(Modifier.height(8.dp))

        when {
            loading -> TerminalLoading("Fetching posts...")
            error != null -> TerminalError(error!!)
            else -> {
                posts.forEachIndexed { idx, post ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://reddit.com${post.permalink}")
                                    )
                                )
                            }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Score
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(40.dp)
                        ) {
                            Text(
                                "▲",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                formatNumber(post.score),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = post.title,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "r/${post.subreddit} · u/${post.author} · ${post.numComments} comments · ${timeAgo(post.createdAt)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    if (idx < posts.lastIndex) TerminalDivider()
                }
            }
        }
    }
}
