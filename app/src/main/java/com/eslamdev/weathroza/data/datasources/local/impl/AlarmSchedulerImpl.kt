package com.eslamdev.weathroza.data.datasources.local.impl


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.eslamdev.weathroza.core.receiver.AlertReceiver
import com.eslamdev.weathroza.data.datasources.local.AlarmScheduler
import com.eslamdev.weathroza.data.models.alert.AlertEntity
import java.util.Date

class AlarmSchedulerImpl(
    private val context: Context,
    private val alarmManager: AlarmManager,
) : AlarmScheduler {

    override fun scheduleAlert(alert: AlertEntity) {
        val startTime = alert.startTimeMillis ?: run {
            Log.w(
                "AlarmScheduler",
                "scheduleAlert: startTimeMillis is null for alert id=${alert.id}, skipping."
            )
            return
        }

        Log.d(
            "AlarmScheduler", "scheduleAlert: id=${alert.id}, name=${alert.name}, " +
                    "scheduledAt=${Date(startTime)}, endAt=${alert.endTimeMillis?.let { Date(it) }}"
        )

        val intent = Intent(context, AlertReceiver::class.java).apply {
            action = AlertReceiver.ACTION_ALERT_START
            putExtra(AlertReceiver.EXTRA_ALERT_ID, alert.id)
            putExtra(AlertReceiver.EXTRA_ALERT_NAME, alert.name)
            putExtra(AlertReceiver.EXTRA_START_MILLIS, alert.startTimeMillis)
            putExtra(AlertReceiver.EXTRA_END_MILLIS, alert.endTimeMillis)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alert.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            Log.w(
                "AlarmScheduler",
                "scheduleAlert: No exact alarm permission — using inexact alarm for id=${alert.id}"
            )
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, startTime, pendingIntent)
        } else {
            Log.d("AlarmScheduler", "scheduleAlert: Exact alarm set for id=${alert.id}")
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                startTime,
                pendingIntent
            )
        }
    }

    override fun cancelAlert(alertId: Long) {
        Log.d("AlarmScheduler", "cancelAlert: cancelling alarm for id=$alertId")

        val intent = Intent(context, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alertId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)

        Log.d("AlarmScheduler", "cancelAlert: alarm cancelled for id=$alertId")
    }
}