// data/datasources/local/impl/WorkManagerAlertScheduler.kt
package com.eslamdev.weathroza.data.datasources.local.impl

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.eslamdev.weathroza.data.datasources.local.AlarmScheduler
import com.eslamdev.weathroza.data.datasources.local.worker.AlertCheckWorker
import com.eslamdev.weathroza.data.datasources.local.worker.ContinuousAlertWorker
import com.eslamdev.weathroza.data.models.alert.AlertEntity
import java.util.concurrent.TimeUnit

class WorkManagerAlertScheduler(
    private val context: Context,
) : AlarmScheduler {

    private val workManager by lazy { WorkManager.getInstance(context) }

    private val networkConstraint = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    // ── TIME_BASED ───────────────────────────────────────────────

    override fun scheduleAlert(alert: AlertEntity) {
        val startTime = alert.startTimeMillis ?: return
        val delay = startTime - System.currentTimeMillis()
        if (delay < 0) return

        val request = OneTimeWorkRequestBuilder<AlertCheckWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(Data.Builder().putLong(AlertCheckWorker.KEY_ALERT_ID, alert.id).build())
            .setConstraints(networkConstraint)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
            .addTag("alert_${alert.id}")
            .build()

        workManager.enqueueUniqueWork(
            "alert_${alert.id}",
            ExistingWorkPolicy.REPLACE,
            request,
        )
    }

    override fun cancelAlert(alertId: Long) {
        workManager.cancelUniqueWork("alert_$alertId")
    }

    // ── CONTINUOUS ───────────────────────────────────────────────

    override fun scheduleContinuousAlerts() {
        Log.i("TAG", "scheduleContinuousAlerts: begun")
        val request = PeriodicWorkRequestBuilder<ContinuousAlertWorker>(1, TimeUnit.HOURS)
            .setConstraints(networkConstraint)
            .setInitialDelay(0, TimeUnit.SECONDS)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
            .addTag(CONTINUOUS_WORK_TAG)
            .build()

        workManager.enqueueUniquePeriodicWork(
            CONTINUOUS_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request,
        )
    }

    fun cancelContinuousAlerts() {
        workManager.cancelUniqueWork(CONTINUOUS_WORK_NAME)
    }

    companion object {
        const val CONTINUOUS_WORK_NAME = "continuous_alerts_worker"
        const val CONTINUOUS_WORK_TAG = "continuous_alerts"
    }
}