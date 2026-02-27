package com.eslamdev.weathroza.data.models.alert


import androidx.annotation.StringRes
import com.eslamdev.weathroza.R

enum class AlertDay(@StringRes val labelRes: Int) {
    MON(R.string.day_mon),
    TUE(R.string.day_tue),
    WED(R.string.day_wed),
    THU(R.string.day_thu),
    FRI(R.string.day_fri),
    SAT(R.string.day_sat),
    SUN(R.string.day_sun),
}