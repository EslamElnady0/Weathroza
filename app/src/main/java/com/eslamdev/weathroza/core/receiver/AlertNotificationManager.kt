package com.eslamdev.weathroza.core.receiver

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.eslamdev.weathroza.R

class AlertNotificationManager(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannels()
    }

    // ── Public ───────────────────────────────────────────────────
    @SuppressLint("RemoteViewLayout")
    fun show(alertId: Long, alertName: String, endMillis: Long, isAlarm: Boolean = true) {
        val durationText = if (endMillis > 0) {
            val durationMinutes = (endMillis - System.currentTimeMillis()) / 60_000
            "Active for ~$durationMinutes min"
        } else {
            "Active now"
        }

        val remoteViews = RemoteViews(context.packageName, R.layout.notification_alert).apply {
            setTextViewText(R.id.tv_title, alertName)
            setTextViewText(R.id.tv_duration, durationText)
            setOnClickPendingIntent(R.id.btn_dismiss, buildDismissPendingIntent(alertId))
        }

        val channelId = if (isAlarm) ALARM_CHANNEL_ID else CHANNEL_ID

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.sunrise_ic)
            .setContentTitle("⚠️ $alertName")
            .setContentText(durationText)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(
                buildFullScreenPendingIntent(alertId, alertName, endMillis),
                isAlarm
            )
            .setOngoing(isAlarm)
            .setAutoCancel(true)
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

    private fun buildFullScreenPendingIntent(
        alertId: Long,
        alertName: String,
        endMillis: Long
    ): PendingIntent {
        val intent = Intent(context, FullScreenAlertActivity::class.java).apply {
            putExtra(AlertReceiver.EXTRA_ALERT_ID, alertId)
            putExtra(AlertReceiver.EXTRA_ALERT_NAME, alertName)
            putExtra(AlertReceiver.EXTRA_END_MILLIS, endMillis)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return PendingIntent.getActivity(
            context,
            alertId.toInt() + 3000,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val normalChannel = NotificationChannel(
                CHANNEL_ID,
                "Weather Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "General weather notifications"
                enableVibration(false)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }

            val alarmChannel = NotificationChannel(
                ALARM_CHANNEL_ID,
                "Weather Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Urgent scheduled weather alerts"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }

            notificationManager.createNotificationChannel(normalChannel)
            notificationManager.createNotificationChannel(alarmChannel)
        }
    }

    companion object {
        const val CHANNEL_ID = "weather_notifications_channel"
        const val ALARM_CHANNEL_ID = "weather_alerts_channel_v2"
    }
}