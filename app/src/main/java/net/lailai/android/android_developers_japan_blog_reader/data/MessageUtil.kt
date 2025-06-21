package net.lailai.android.android_developers_japan_blog_reader.data

import android.content.Context
import net.lailai.android.android_developers_japan_blog_reader.R
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object MessageUtil {
    fun mapToErrorMessage(context: Context, e: Throwable): String {
        return when (e) {
            is UnknownHostException, is ConnectException -> context.getString(R.string.msg_error_no_network)
            is retrofit2.HttpException -> {
                if (e.code() >= 500) {
                    context.getString(R.string.msg_error_internal_server_error)
                } else if (e.code() == 401 || e.code() == 403) {
                    context.getString(R.string.msg_error_unauthorized)
                } else {
                    context.getString(R.string.msg_error_normal)
                }
            }

            is SocketTimeoutException -> context.getString(R.string.msg_error_timeout)
            else -> context.getString(R.string.msg_error_unexpected)
        }
    }
}
