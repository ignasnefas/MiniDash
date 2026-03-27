package com.minimalnews.ui.widgets

import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.minimalnews.ui.components.TerminalBox
import com.minimalnews.ui.components.TerminalDivider
import java.util.*

@Composable
fun SystemInfoWidgetComposable() {
    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics

    val info = remember {
        listOf(
            "OS" to "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})",
            "Device" to "${Build.MANUFACTURER} ${Build.MODEL}",
            "Brand" to Build.BRAND,
            "Hardware" to Build.HARDWARE,
            "Screen" to "${displayMetrics.widthPixels}x${displayMetrics.heightPixels}",
            "Density" to "${displayMetrics.density}x (${displayMetrics.densityDpi} dpi)",
            "CPU Cores" to "${Runtime.getRuntime().availableProcessors()}",
            "Max Memory" to "${Runtime.getRuntime().maxMemory() / (1024 * 1024)} MB",
            "Language" to Locale.getDefault().displayLanguage,
            "Timezone" to TimeZone.getDefault().id,
            "Architecture" to (Build.SUPPORTED_ABIS.firstOrNull() ?: "Unknown"),
            "Board" to Build.BOARD,
        )
    }

    TerminalBox(title = "systeminfo") {
        info.forEach { (key, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = key,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(100.dp)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f)
                )
            }
            TerminalDivider()
        }
    }
}
