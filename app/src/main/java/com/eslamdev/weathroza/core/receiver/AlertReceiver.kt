// core/receiver/AlertReceiver.kt
package com.eslamdev.weathroza.core.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import com.eslamdev.weathroza.core.notification.AlertNotificationManager
import com.eslamdev.weathroza.data.models.alert.AlertNotifyType
import java.util.Date

class AlertReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_ALERT_ID = "alert_id"
        const val EXTRA_ALERT_NAME = "alert_name"
        const val EXTRA_START_MILLIS = "start_millis"
        const val EXTRA_END_MILLIS = "end_millis"
        const val EXTRA_NOTIFICATION_TITLE = "notification_title"
        const val EXTRA_NOTIFICATION_BODY = "notification_body"
        const val EXTRA_DURATION_TEXT = "duration_text"
        const val EXTRA_NOTIFY_TYPE = "notify_type"
        const val ACTION_ALERT_START = "com.eslamdev.weathroza.ACTION_ALERT_START"
        const val ACTION_ALERT_NOT_MET = "com.eslamdev.weathroza.ACTION_ALERT_NOT_MET"
        const val ACTION_ALERT_END = "com.eslamdev.weathroza.ACTION_ALERT_END"
        const val ACTION_DISMISS = "com.eslamdev.weathroza.ACTION_DISMISS_ALERT"
        const val EXTRA_NOTIFICATION_ID = "notification_id"

        private var activeRingtone: Ringtone? = null
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_ALERT_START -> handleAlertStart(context, intent)
            ACTION_ALERT_NOT_MET -> handleAlertNotMet(context, intent)
            ACTION_ALERT_END,
            ACTION_DISMISS -> handleAlertEnd(context, intent)

            else -> Log.w("AlertReceiver", "Unknown action: ${intent.action}")
        }
    }

    // ── Handlers ─────────────────────────────────────────────────

    private fun handleAlertStart(context: Context, intent: Intent) {
        val alertId = intent.getLongExtra(EXTRA_ALERT_ID, -1L)
        val alertName = intent.getStringExtra(EXTRA_ALERT_NAME) ?: "Weather Alert"
        val endMillis = intent.getLongExtra(EXTRA_END_MILLIS, -1L)
        val title = intent.getStringExtra(EXTRA_NOTIFICATION_TITLE) ?: alertName
        val body = intent.getStringExtra(EXTRA_NOTIFICATION_BODY) ?: ""
        val durationText = intent.getStringExtra(EXTRA_DURATION_TEXT) ?: ""
        val notifyType = intent.getStringExtra(EXTRA_NOTIFY_TYPE)
            ?.let { runCatching { AlertNotifyType.valueOf(it) }.getOrNull() }
            ?: AlertNotifyType.ALARM

        Log.d(
            "AlertReceiver",
            "START: id=$alertId notifyType=$notifyType endAt=${if (endMillis > 0) Date(endMillis) else "none"}"
        )

        val notificationManager = AlertNotificationManager(context)

        when (notifyType) {
            AlertNotifyType.ALARM -> {
                notificationManager.showAlertMet(alertId, title, body, durationText, endMillis)
                playAlarmSound(context)
                scheduleEndAlarm(context, alertId, endMillis)
            }

            AlertNotifyType.NOTIFICATION_SOUND -> {
                notificationManager.showAlertMetAsNotification(alertId, title, body)
            }
        }
    }

    private fun handleAlertNotMet(context: Context, intent: Intent) {
        val alertId = intent.getLongExtra(EXTRA_ALERT_ID, -1L)
        val title = intent.getStringExtra(EXTRA_NOTIFICATION_TITLE) ?: "Weather Check"
        val body = intent.getStringExtra(EXTRA_NOTIFICATION_BODY) ?: ""

        Log.d("AlertReceiver", "NOT MET: id=$alertId")

        AlertNotificationManager(context).showAlertNotMet(alertId, title, body)
    }

    private fun handleAlertEnd(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1)
        Log.d("AlertReceiver", "END/DISMISS: id=$notificationId")

        stopAlarmSound()
        AlertNotificationManager(context).cancel(notificationId.toLong())
    }

    // ── Sound ────────────────────────────────────────────────────

    private fun playAlarmSound(context: Context) {
        try {
            activeRingtone?.stop()
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            activeRingtone = RingtoneManager.getRingtone(context, soundUri).also {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) it.isLooping = true
                it.play()
            }
        } catch (e: Exception) {
            Log.e("AlertReceiver", "Failed to play alarm sound", e)
        }
    }

    private fun stopAlarmSound() {
        activeRingtone?.stop()
        activeRingtone = null
    }

    // ── End Alarm ────────────────────────────────────────────────

    private fun scheduleEndAlarm(context: Context, alertId: Long, endMillis: Long) {
        if (endMillis <= 0) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val endIntent = Intent(context, AlertReceiver::class.java).apply {
            action = ACTION_ALERT_END
            putExtra(EXTRA_NOTIFICATION_ID, alertId.toInt())
        }
        val endPendingIntent = PendingIntent.getBroadcast(
            context,
            alertId.toInt() + 2000,
            endIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, endMillis, endPendingIntent)
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                endMillis,
                endPendingIntent
            )
        }

        Log.d("AlertReceiver", "End alarm scheduled for: ${Date(endMillis)}")
    }
}