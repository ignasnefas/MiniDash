package com.minimalnews.appwidgets

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.minimalnews.data.repository.Repository

class WidgetDataWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val type = inputData.getString("widget_type") ?: return Result.failure()
        val repo = Repository(applicationContext)
        val gson = Gson()

        return try {
            when (type) {
                "weather" -> {
                    val location = repo.prefs.getString("weather_location", "New York") ?: "New York"
                    val data = repo.fetchWeather(location)
                    repo.saveWidgetData("weather_data", gson.toJson(data))
                    triggerWidgetUpdate(WeatherAppWidget::class.java)
                }
                "news" -> {
                    val data = repo.fetchNews()
                    repo.saveWidgetData("news_data", gson.toJson(data))
                    triggerWidgetUpdate(NewsAppWidget::class.java)
                }
                "crypto" -> {
                    val data = repo.fetchCrypto()
                    repo.saveWidgetData("crypto_data", gson.toJson(data))
                    triggerWidgetUpdate(CryptoAppWidget::class.java)
                }
                "quote" -> {
                    val data = repo.fetchQuote()
                    repo.saveWidgetData("quote_data", gson.toJson(data))
                    triggerWidgetUpdate(QuoteAppWidget::class.java)
                }
                "hackernews" -> {
                    val data = repo.fetchHackerNews()
                    repo.saveWidgetData("hn_data", gson.toJson(data))
                    triggerWidgetUpdate(HackerNewsAppWidget::class.java)
                }
            }
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }

    private fun triggerWidgetUpdate(widgetClass: Class<*>) {
        val manager = AppWidgetManager.getInstance(applicationContext)
        val ids = manager.getAppWidgetIds(ComponentName(applicationContext, widgetClass))
        if (ids.isNotEmpty()) {
            val intent = Intent(applicationContext, widgetClass).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            }
            applicationContext.sendBroadcast(intent)
        }
    }
}
