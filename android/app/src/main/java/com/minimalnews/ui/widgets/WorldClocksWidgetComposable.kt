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
import androidx.compose.ui.unit.dp
import com.minimalnews.ui.components.TerminalBox
import com.minimalnews.ui.components.TerminalDivider
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WorldClocksWidgetComposable() {
    var zones by remember {
        mutableStateOf(
            listOf("UTC", "America/New_York", "Europe/London", "Asia/Tokyo")
        )
    }
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var newZone by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = System.currentTimeMillis()
            delay(1000)
        }
    }

    TerminalBox(title = "world-clocks", status = "${zones.size} zones") {
        zones.forEach { zoneId ->
            val tz = TimeZone.getTimeZone(zoneId)
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            sdf.timeZone = tz
            val dateFmt = SimpleDateFormat("MMM dd", Locale.getDefault())
            dateFmt.timeZone = tz

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = zoneId.replace("_", " "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = dateFmt.format(Date(currentTime)),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = sdf.format(Date(currentTime)),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                if (zones.size > 1) {
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "✕",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.clickable {
                            zones = zones.filter { it != zoneId }
                        }
                    )
                }
            }
            TerminalDivider()
        }

        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "$ add: ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            OutlinedTextField(
                value = newZone,
                onValueChange = { newZone = it },
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                singleLine = true,
                placeholder = {
                    Text(
                        "e.g. Europe/Paris",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    val tz = TimeZone.getTimeZone(newZone)
                    if (tz.id != "GMT" || newZone.equals("GMT", true)) {
                        zones = zones + newZone
                        newZone = ""
                    }
                }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    cursorColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    }
}
