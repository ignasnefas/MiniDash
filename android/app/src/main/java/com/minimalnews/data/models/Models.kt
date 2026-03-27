package com.minimalnews.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

// Weather
data class WeatherData(
    val location: String,
    val current: CurrentWeather,
    val forecast: List<ForecastDay>
)

data class CurrentWeather(
    val temp: Double,
    val feelsLike: Double,
    val humidity: Int,
    val windSpeed: Double,
    val windDirection: String,
    val condition: String,
    val icon: String,
    val visibility: Double,
    val pressure: Double
)

data class ForecastDay(
    val date: String,
    val high: Double,
    val low: Double,
    val condition: String,
    val icon: String,
    val precipitation: Double
)

// News
data class NewsItem(
    val id: String,
    val title: String,
    val source: String,
    val url: String,
    val publishedAt: String,
    val category: String? = null
)

// Reddit
data class RedditPost(
    val id: String,
    val title: String,
    val subreddit: String,
    val score: Int,
    val numComments: Int,
    val url: String,
    val permalink: String,
    val author: String,
    val createdAt: Long
)

// HackerNews
data class HackerNewsItem(
    val id: Long = 0,
    val title: String = "",
    val url: String? = null,
    val score: Int = 0,
    val by: String = "",
    val time: Long = 0,
    val descendants: Int = 0,
    val type: String = ""
)

// Crypto
data class CryptoPrice(
    val id: String,
    val symbol: String,
    val name: String,
    val price: Double,
    val change24h: Double,
    val change7d: Double? = null,
    val change30d: Double? = null
)

// Quote
data class Quote(
    val text: String = "",
    val author: String? = null
)

// GitHub Trending
data class GitHubTrending(
    val name: String,
    val description: String,
    val language: String,
    val stars: Int,
    val url: String
)

// Todo
@Entity(tableName = "todos")
data class TodoItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val completed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

// Widget Config
data class WidgetConfig(
    val id: String,
    val name: String,
    val enabled: Boolean = true
)
