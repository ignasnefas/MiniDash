package com.minimalnews

import android.app.Application
import androidx.work.*
import com.minimalnews.appwidgets.WidgetDataWorker
import java.util.concurrent.TimeUnit

class MinimalNewsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        schedulePeriodicWidgetUpdates()
    }

    private fun schedulePeriodicWidgetUpdates() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val widgetTypes = listOf("weather", "news", "crypto", "quote", "hackernews")
        widgetTypes.forEach { type ->
            val work = PeriodicWorkRequestBuilder<WidgetDataWorker>(30, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInputData(workDataOf("widget_type" to type))
                .build()
            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "widget_update_$type",
                ExistingPeriodicWorkPolicy.KEEP,
                work
            )
        }
    }
}
