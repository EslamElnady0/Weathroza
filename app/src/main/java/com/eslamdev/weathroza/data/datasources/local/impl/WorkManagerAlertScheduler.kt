package com.eslamdev.weathroza.data.datasources.local.impl

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.eslamdev.weathroza.data.datasources.local.AlarmScheduler
import com.eslamdev.weathroza.data.datasources.local.worker.AlertCheckWorker
import com.eslamdev.weathroza.data.models.alert.AlertEntity
import java.util.concurrent.TimeUnit

class WorkManagerAlertScheduler(
    private val context: Context,
) : AlarmScheduler {

    override fun scheduleAlert(alert: AlertEntity) {
        val startTime = alert.startTimeMillis ?: return
        val delay = startTime - System.currentTimeMillis()
        if (delay < 0) return

        val inputData = Data.Builder()
            .putLong(AlertCheckWorker.KEY_ALERT_ID, alert.id)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<AlertCheckWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                1,
                TimeUnit.MINUTES,
            )
            .addTag("alert_${alert.id}")
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                "alert_${alert.id}",
                ExistingWorkPolicy.REPLACE,
                request,
            )
    }

    override fun cancelAlert(alertId: Long) {
        WorkManager.getInstance(context)
            .cancelUniqueWork("alert_$alertId")
    }
}