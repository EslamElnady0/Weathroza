package com.eslamdev.weathroza.core.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.receiver.AlertReceiver
import com.eslamdev.weathroza.presentaion.alerts.views.FullScreenAlertActivity

class AlertNotificationManager(private val context: Context) {

    private val notificationHelper = NotificationHelper(context)

    init {
        createChannels()
    }

    // ── Public ───────────────────────────────────────────────────

    fun showAlertMet(
        alertId: Long,
        title: String,
        body: String,
        durationText: String,
        endMillis: Long,
    ) {
        val notification = notificationHelper.buildNotification(
            channelId = ALARM_CHANNEL_ID,
            title = title,
            body = "$body\n$durationText",
            smallIcon = R.drawable.sunrise_ic,
            priority = NotificationCompat.PRIORITY_MAX,
            ongoing = true,
            autoCancel = false,
            vibrate = longArrayOf(0, 500, 200, 500),
            fullScreenIntent = buildFullScreenIntent(alertId, title, endMillis),
            actions = listOf(
                NotificationCompat.Action(
                    R.drawable.sunrise_ic,
                    context.getString(R.string.dismiss),
                    buildDismissIntent(alertId),
                )
            ),
        )
        notificationHelper.show(alertId.toInt(), notification)
    }

    fun showAlertNotMet(alertId: Long, title: String, body: String) {
        val notification = notificationHelper.buildNotification(
            channelId = CHANNEL_ID,
            title = title,
            body = body,
            smallIcon = R.drawable.sunrise_ic,
            priority = NotificationCompat.PRIORITY_HIGH,
            ongoing = false,
            autoCancel = true,
        )
        notificationHelper.show(alertId.toInt(), notification)
    }


    fun showAlertMetAsNotification(alertId: Long, title: String, body: String) {
        val notification = notificationHelper.buildNotification(
            channelId = ALARM_CHANNEL_ID,
            title = title,
            body = body,
            smallIcon = R.drawable.sunrise_ic,
            priority = NotificationCompat.PRIORITY_HIGH,
            ongoing = true,
            autoCancel = true,
        )
        notificationHelper.show(alertId.toInt(), notification)
    }

    fun cancel(alertId: Long) {
        notificationHelper.cancel(alertId.toInt())
    }

    // ── Channels ─────────────────────────────────────────────────
    fun initChannels() = createChannels()

    private fun createChannels() {
        notificationHelper.createChannel(
            channelId = CHANNEL_ID,
            channelName = "Weather Notifications",
            importance = NotificationManager.IMPORTANCE_HIGH,
            description = "General weather check results",
            enableVibration = false,
        )
        notificationHelper.createChannel(
            channelId = ALARM_CHANNEL_ID,
            channelName = "Weather Alerts",
            importance = NotificationManager.IMPORTANCE_HIGH,
            description = "Urgent scheduled weather alerts",
            enableVibration = true,
            vibrationPattern = longArrayOf(0, 500, 200, 500),
        )
    }

    // ── PendingIntents ───────────────────────────────────────────

    private fun buildDismissIntent(alertId: Long): PendingIntent {
        val intent = Intent(context, AlertReceiver::class.java).apply {
            action = AlertReceiver.ACTION_DISMISS
            putExtra(AlertReceiver.EXTRA_NOTIFICATION_ID, alertId.toInt())
        }
        return PendingIntent.getBroadcast(
            context,
            alertId.toInt() + 1000,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun buildFullScreenIntent(
        alertId: Long,
        alertName: String,
        endMillis: Long,
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
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    companion object {
        const val CHANNEL_ID = "weather_notifications_channel"
        const val ALARM_CHANNEL_ID = "weather_alerts_channel_v2"
    }
}