package com.eslamdev.weathroza.core.network

import android.content.Context
import com.eslamdev.weathroza.R
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorHandler {
    fun handleException(exception: Exception, context: Context): String {
        return when (exception) {
            is UnknownHostException -> context.getString(R.string.error_no_internet)
            is SocketTimeoutException -> context.getString(R.string.error_timeout)
            is IOException -> context.getString(R.string.error_no_internet)
            is HttpException -> {
                when (exception.code()) {
                    in 500..599 -> context.getString(R.string.error_server)
                    else -> context.getString(R.string.error_unknown)
                }
            }
            else -> context.getString(R.string.error_unknown)
        }
    }
}
