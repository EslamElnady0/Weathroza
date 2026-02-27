package com.eslamdev.weathroza.data.models.alert

import androidx.annotation.StringRes
import com.eslamdev.weathroza.R

enum class AlertFrequency(@StringRes val labelRes: Int) {
    TIME_BASED(R.string.time_based),
    CONTINUOUS(R.string.continuous),
}