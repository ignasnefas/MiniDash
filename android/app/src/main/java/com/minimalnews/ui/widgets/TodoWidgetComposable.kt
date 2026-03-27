package com.minimalnews.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.minimalnews.data.local.AppDatabase
import com.minimalnews.data.models.TodoItem
import com.minimalnews.ui.components.TerminalBox
import com.minimalnews.ui.components.TerminalDivider
import kotlinx.coroutines.launch

@Composable
fun TodoWidgetComposable(database: AppDatabase) {
    val todos by database.todoDao().getAll().collectAsState(initial = emptyList())
    var newTodoText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val completed = todos.count { it.completed }

    TerminalBox(
        title = "todo",
        status = "$completed/${todos.size} done"
    ) {
        // Add new todo
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "$ new: ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            OutlinedTextField(
                value = newTodoText,
                onValueChange = { newTodoText = it },
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                singleLine = true,
                placeholder = {
                    Text(
                        "Add a task...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (newTodoText.isNotBlank()) {
                        scope.launch {
                            database.todoDao().insert(TodoItem(text = newTodoText.trim()))
                        }
                        newTodoText = ""
                    }
                }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    cursorColor = MaterialTheme.colorScheme.primary,
                )
            )
        }

        Spacer(Modifier.height(8.dp))

        if (todos.isEmpty()) {
            Text(
                "No tasks yet.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            todos.forEach { todo ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope.launch {
                                database.todoDao().update(todo.copy(completed = !todo.completed))
                            }
                        }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (todo.completed) "[x]" else "[ ]",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (todo.completed) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.width(28.dp)
                    )
                    Text(
                        text = todo.text,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (todo.completed) MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.onBackground,
                        textDecoration = if (todo.completed) TextDecoration.LineThrough else null,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "✕",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.clickable {
                            scope.launch { database.todoDao().delete(todo) }
                        }
                    )
                }
                TerminalDivider()
            }
        }
    }
}
