package com.minimalnews.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.minimalnews.data.local.AppDatabase
import com.minimalnews.data.repository.Repository
import com.minimalnews.ui.theme.terminalThemes

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val repository = Repository(application)
    val database = AppDatabase.getInstance(application)

    val allWidgetIds = listOf(
        "quote", "clock", "weather", "news", "hackernews",
        "reddit", "crypto", "worldclocks", "todo", "trending", "systeminfo"
    )

    private val defaultEnabled = listOf(
        "quote", "clock", "weather", "news", "hackernews",
        "reddit", "crypto", "worldclocks", "todo"
    )

    private val prefs = application.getSharedPreferences("app_prefs", 0)

    var enabledWidgets = mutableStateOf(
        prefs.getStringSet("enabled_widgets", null)?.toList() ?: defaultEnabled
    )
        private set

    var currentTheme = mutableStateOf(
        prefs.getString("theme", "dark") ?: "dark"
    )
        private set

    var widgetManagerOpen = mutableStateOf(false)

    fun toggleWidget(id: String) {
        val current = enabledWidgets.value.toMutableList()
        if (current.contains(id)) current.remove(id) else current.add(id)
        enabledWidgets.value = current
        saveWidgets()
    }

    fun moveWidget(id: String, direction: String) {
        val list = enabledWidgets.value.toMutableList()
        val idx = list.indexOf(id)
        if (idx < 0) return
        val newIdx = if (direction == "up") (idx - 1).coerceAtLeast(0)
        else (idx + 1).coerceAtMost(list.lastIndex)
        list.removeAt(idx)
        list.add(newIdx, id)
        enabledWidgets.value = list
        saveWidgets()
    }

    fun enableAll() {
        enabledWidgets.value = allWidgetIds.toList()
        saveWidgets()
    }

    fun disableAll() {
        enabledWidgets.value = emptyList()
        saveWidgets()
    }

    fun resetWidgets() {
        enabledWidgets.value = defaultEnabled
        saveWidgets()
    }

    fun cycleTheme() {
        val themes = terminalThemes.map { it.name }
        val idx = themes.indexOf(currentTheme.value)
        currentTheme.value = themes[(idx + 1) % themes.size]
        prefs.edit().putString("theme", currentTheme.value).apply()
    }

    fun setTheme(name: String) {
        currentTheme.value = name
        prefs.edit().putString("theme", name).apply()
    }

    private fun saveWidgets() {
        prefs.edit().putStringSet("enabled_widgets", enabledWidgets.value.toSet()).apply()
    }

    fun widgetDisplayName(id: String): String = when (id) {
        "quote" -> "Quote"
        "clock" -> "Clock"
        "weather" -> "Weather"
        "news" -> "News"
        "hackernews" -> "Hacker News"
        "reddit" -> "Reddit"
        "crypto" -> "Crypto"
        "worldclocks" -> "World Clocks"
        "todo" -> "Todo"
        "trending" -> "Trending"
        "systeminfo" -> "System Info"
        else -> id
    }
}
