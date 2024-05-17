package com.lange.facilitytracker.data.remote
import okhttp3.Interceptor
import okhttp3.Response
import android.util.Log

class RequestLoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Log cookies being sent to the server
        request.headers("Cookie").forEach {
            Log.d("RequestLoggingInterceptor", "Sending cookie: $it")
        }

        return chain.proceed(request)
    }
}
