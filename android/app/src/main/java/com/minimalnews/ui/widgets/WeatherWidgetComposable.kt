package com.minimalnews.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.minimalnews.data.models.WeatherData
import com.minimalnews.data.repository.Repository
import com.minimalnews.ui.components.*
import kotlinx.coroutines.launch

@Composable
fun WeatherWidgetComposable(repository: Repository) {
    var weather by remember { mutableStateOf<WeatherData?>(null) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var locationInput by remember { mutableStateOf("New York") }
    var searchLocation by remember { mutableStateOf("New York") }
    val scope = rememberCoroutineScope()

    fun loadWeather(location: String) {
        scope.launch {
            loading = true; error = null
            try {
                weather = repository.fetchWeather(location)
                repository.saveWidgetData("weather_location", location)
            } catch (e: Exception) {
                error = e.message ?: "Failed to load weather"
            }
            loading = false
        }
    }

    LaunchedEffect(searchLocation) {
        loadWeather(searchLocation)
    }

    TerminalBox(
        title = "weather",
        status = weather?.location ?: "",
        onRefresh = { loadWeather(searchLocation) }
    ) {
        // Location input
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "$ location: ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            OutlinedTextField(
                value = locationInput,
                onValueChange = { locationInput = it },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    searchLocation = locationInput
                }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    cursorColor = MaterialTheme.colorScheme.primary,
                )
            )
        }

        Spacer(Modifier.height(12.dp))

        when {
            loading -> TerminalLoading("Fetching weather data...")
            error != null -> TerminalError(error!!)
            weather != null -> {
                val w = weather!!
                // Current weather
                Text(
                    text = "${w.current.icon} ${w.current.temp.toInt()}°C — ${w.current.condition}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Feels like ${w.current.feelsLike.toInt()}°C",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))

                // Details
                val details = listOf(
                    "Humidity" to "${w.current.humidity}%",
                    "Wind" to "${w.current.windSpeed} km/h ${w.current.windDirection}",
                    "Pressure" to "${w.current.pressure.toInt()} hPa"
                )
                details.forEach { (label, value) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 1.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = value,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                TerminalDivider()

                // Forecast
                Text(
                    text = "5-Day Forecast",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(4.dp))
                w.forecast.forEach { day ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = day.date.takeLast(5),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.width(48.dp)
                        )
                        Text(
                            text = day.icon,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "${day.low.toInt()}°/${day.high.toInt()}°",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.width(64.dp)
                        )
                        Text(
                            text = day.condition,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
