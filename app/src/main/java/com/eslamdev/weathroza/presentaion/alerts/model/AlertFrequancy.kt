package com.eslamdev.weathroza.presentaion.alerts.model

import androidx.annotation.StringRes
import com.eslamdev.weathroza.R

enum class AlertFrequency(@StringRes val labelRes: Int) {
    ONE_TIME(R.string.one_time),
    PERIODIC(R.string.periodic),
}