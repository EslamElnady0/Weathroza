package com.eslamdev.weathroza.core.network

import com.eslamdev.weathroza.R
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorHandler {
    fun handleException(exception: Exception): Int {
        return when (exception) {
            is UnknownHostException -> R.string.error_no_internet
            is SocketTimeoutException -> R.string.error_timeout
            is IOException -> R.string.error_no_internet
            is HttpException -> {
                when (exception.code()) {
                    in 500..599 -> R.string.error_server
                    else -> R.string.error_unknown
                }
            }

            else -> R.string.error_unknown
        }
    }
}
