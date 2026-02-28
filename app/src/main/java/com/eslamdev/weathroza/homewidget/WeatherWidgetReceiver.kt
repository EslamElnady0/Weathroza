package com.eslamdev.weathroza.homewidget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class WeatherWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WeatherWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        WeatherWidgetWorker.scheduleImmediate(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
    }
}