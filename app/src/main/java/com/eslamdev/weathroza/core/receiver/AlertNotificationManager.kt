package com.eslamdev.weathroza.core.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.eslamdev.weathroza.R

class AlertNotificationManager(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    // ── Public ───────────────────────────────────────────────────

    fun show(alertId: Long, alertName: String, endMillis: Long) {
        val durationText = if (endMillis > 0) {
            val durationMinutes = (endMillis - System.currentTimeMillis()) / 60_000
            "Active for ~$durationMinutes min"
        } else {
            "Active now"
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.sunrise_ic)
            .setContentTitle("⚠️ Weather Alert: $alertName")
            .setContentText(durationText)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(null, true)
            .setOngoing(true)
            .setAutoCancel(false)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .addAction(R.drawable.sunrise_ic, "Dismiss", buildDismissPendingIntent(alertId))
            .build()

        notificationManager.notify(alertId.toInt(), notification)
    }

    fun cancel(alertId: Long) {
        notificationManager.cancel(alertId.toInt())
    }

    // ── Private ──────────────────────────────────────────────────

    private fun buildDismissPendingIntent(alertId: Long): PendingIntent {
        val intent = Intent(context, AlertReceiver::class.java).apply {
            action = AlertReceiver.ACTION_DISMISS
            putExtra(AlertReceiver.EXTRA_NOTIFICATION_ID, alertId.toInt())
        }
        return PendingIntent.getBroadcast(
            context,
            alertId.toInt() + 1000,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Weather Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for scheduled weather alerts"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "weather_alerts_channel_v2"
    }
}