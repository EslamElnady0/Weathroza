package com.eslamdev.weathroza.core.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.eslamdev.weathroza.MainActivity

class NotificationHelper(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // ── Channel ──────────────────────────────────────────────────

    fun createChannel(
        channelId: String,
        channelName: String,
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
        description: String = "",
        enableVibration: Boolean = false,
        vibrationPattern: LongArray? = null,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                this.description = description
                this.enableVibration(enableVibration)
                if (!enableVibration) setSound(null, null)
                vibrationPattern?.let { this.vibrationPattern = it }
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    // ── Build ────────────────────────────────────────────────────

    fun buildNotification(
        channelId: String,
        title: String,
        body: String,
        smallIcon: Int,
        priority: Int = NotificationCompat.PRIORITY_DEFAULT,
        ongoing: Boolean = false,
        autoCancel: Boolean = true,
        vibrate: LongArray? = null,
        fullScreenIntent: PendingIntent? = null,
        actions: List<NotificationCompat.Action> = emptyList(),
    ): Notification {
        val contentIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(smallIcon)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(priority)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(ongoing)
            .setAutoCancel(autoCancel)
            .setContentIntent(contentIntent)

        vibrate?.let { builder.setVibrate(it) }
        fullScreenIntent?.let { builder.setFullScreenIntent(it, true) }
        actions.forEach { builder.addAction(it) }

        return builder.build()
    }

    // ── Show / Cancel ────────────────────────────────────────────

    fun show(notificationId: Int, notification: Notification) {
        notificationManager.notify(notificationId, notification)
    }

    fun cancel(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }
}